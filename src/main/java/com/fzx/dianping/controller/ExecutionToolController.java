package com.fzx.dianping.controller;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import java.util.Map;/*
 * Copyright 2004-2020 Homolo Co., Ltd. All rights reserved.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 */

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 执行工具.
 *
 * @author fanZhengxu
 */
@Slf4j
@Controller
@RequestMapping("/executionTool")
public class ExecutionToolController {
    private static Map<String, Object> threadMap = Maps.newConcurrentMap();
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    @GetMapping("/start")
    @ResponseBody
    public String start() {
        String uuid = UUID.randomUUID().toString();
        ExecutorService executorService = new ThreadPoolExecutor(1, 10, 10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2), new NameTreadFactory(uuid), new ThreadPoolExecutor.AbortPolicy());
        threadMap.put("index", ((Integer) threadMap.getOrDefault("index", 0)) + 1);
        TestExecutionTool testExecutionTool = new TestExecutionTool(uuid);
        executorService.execute(testExecutionTool);
        threadMap.forEach((key, map) -> {
            Map<String, Object> value = (Map<String, Object>) map;
            if (threadMap.get("index") == value.get("index")) {
                return;
            }
            value.put("msg", "");
            threadMap.put(key, value);
        });
        return uuid;
    }

    @RequestMapping(value = "/tryStop", method = RequestMethod.POST)
    @ResponseBody
    public String tryStop(@RequestBody String threadName) {
        Map<String, Object> threadDetails = (Map<String, Object>) threadMap.get(threadName);
        threadDetails.put("tryStop", true);
        threadMap.put(threadName, threadDetails);
        return "正在尝试停止" + threadName + "......";
    }

    private String setTaskName() {
        return "测试任务";
    }

    private String setTaskVersion() {
        return "1.0";
    }

    private List<Condition> setCondition() {
        List<Condition> conditionList = Lists.newCopyOnWriteArrayList();
        conditionList.add(inputCondition("姓名"));
        conditionList.add(inputCondition("年龄"));
        conditionList.add(inputCondition("年龄"));
        conditionList.add(inputCondition("年龄"));
        conditionList.add(inputCondition("年龄"));
        return conditionList;
    }

    @RequestMapping("/getMessage")
    public ModelAndView getMessage() {
        ModelAndView modelAndView = new ModelAndView("/executionTool2");

        StringBuilder htmlContent = new StringBuilder();
        //  顶部任务名称及版本
        htmlContent.append("<nav class=\"navbar navbar-default\" role=\"navigation\">\n" +
                "    <div class=\"container-fluid\" style=\"width: 60%;\">\n" +
                "        <div class=\"navbar-header\">\n" +
                "            <a class=\"navbar-brand\">" + setTaskName() + "</a>\n" +
                "        </div>\n" +
                "        <span class=\"label label-default\" style=\"position: absolute;margin-top: 10px;\">" +
                setTaskVersion() + "</span>\n" +
                "    </div>\n" +
                "</nav>");

        //  条件参数
        AtomicInteger index = new AtomicInteger(1);
        AtomicInteger wrapIndex = new AtomicInteger(1);
        List<Integer> wrapIndexes = Lists.newCopyOnWriteArrayList();
        setCondition().forEach(condition -> {
            int getIndex = index.get();
            if (getIndex == wrapIndex.get()) {
                htmlContent.append("<div>");
                wrapIndex.addAndGet(3);
                wrapIndexes.add(wrapIndex.get());
            }
            ConditionType conditionType = condition.getConditionType();
            if (StringUtils.equals(ConditionType.INPUT.name(), conditionType.name())) {
                if (getIndex == 1 || wrapIndexes.contains(getIndex)) {
                    htmlContent.append("<div class=\"input-group\" style=\"width: 20%;margin-left: 15%;margin-top: 10px;" +
                            "height: 35px;float: left;\">\n" +
                            "        <span class=\"input-group-addon\">" + condition.getConditionName() + "</span>\n" +
                            "        <input type=\"text\" class=\"form-control\">\n" +
                            "    </div>");
                } else {
                    htmlContent.append(" <div class=\"input-group\" style=\"width: 20%;margin-left: 30px;margin-top: 10px;" +
                            "height: 35px;float: left;\">\n" +
                            "        <span class=\"input-group-addon\">" + condition.getConditionName() + "</span>\n" +
                            "        <input type=\"text\" class=\"form-control\">\n" +
                            "    </div>");
                }
            }
            if (getIndex % 3 == 0) {
                htmlContent.append("</div>");
            }
            index.getAndIncrement();
        });

        //  固定按钮（开始执行、清除日志）
        //  清除日志所在高度
        int logMarginTop = index.get() % 3 == 0 ? index.get() / 3 : index.get() % 3 + 1;
        logMarginTop = logMarginTop * 60;
        htmlContent.append("<!-- margin-top:最后一个输入控件所在高度 + 100px-->\n" +
                "<div style=\"position: absolute;margin-left: 60%;margin-top: " +
                logMarginTop + "px;\">\n" +
                "    <button type=\"button\" class=\"btn btn-success\" id=\"start\">开始执行</button>\n" +
                "    <button type=\"button\" class=\"btn btn-warning\" style=\"margin-left: 10px;\" " +
                "id=\"clearMessage\">清除日志</button>\n" + "</div>");

        if (threadMap.isEmpty()) {
            modelAndView.addObject("htmlContent", htmlContent.toString());
            return modelAndView;
        }

        AtomicInteger threadDetailMarginTop = new AtomicInteger(logMarginTop + 20);
        AtomicInteger progressBarMarginTop = new AtomicInteger(logMarginTop + 80);
        AtomicReference<String> msg = new AtomicReference<>("");
        AtomicReference<Boolean> flush = new AtomicReference<>(false);
        threadMap.entrySet().stream().filter(map -> !StringUtils.equals(map.getKey(), "index")).sorted((a, b) -> {
            if (b == null) {
                return 0;
            }
            Map<String, Object> value1 = (Map<String, Object>) a.getValue();
            Map<String, Object> value2 = (Map<String, Object>) b.getValue();
            Integer index1 = (Integer) value1.get("index");
            Integer index2 = (Integer) value2.get("index");
            if (index1 == null || index2 == null) {
                return 0;
            }
            return index1.compareTo(index2);
        }).forEach(map -> {
            String key = map.getKey();
            Map<String, Object> value = (Map<String, Object>) map.getValue();
            Boolean tryStop = (Boolean) value.get("tryStop");
            if (tryStop == null || Boolean.FALSE.equals(tryStop)) {
                flush.set(true);
            }
            Integer progress = (Integer) value.getOrDefault("progress", 0);
            Integer total = (Integer) value.getOrDefault("total", 0);
            NumberFormat numberFormat = NumberFormat.getInstance();
            // 精确到小数点后2位
            numberFormat.setMaximumFractionDigits(2);
            String result = numberFormat.format((float) progress / (float) total * 100);
            htmlContent.append("<div>\n" +
                    "        <!-- margin-top: 清除日志所在高度 +20px -->\n" +
                    "        <div style=\"position: absolute;margin-left: 15%;margin-top: " + threadDetailMarginTop.get() + "px;\">\n" +
                    "            <h3>\n" +
                    "                <span class=\"label label-danger\">" + key + "</span>\n" +
                    "                <span class=\"label label-default\">" +
                    value.getOrDefault("startTime", "") + "</span>\n" +
                    "                --\n" +
                    "                <span class=\"label label-default\">" +
                    value.getOrDefault("endTime", "") + "</span>\n" +
                    "            </h3>\n" +
                    "        </div>\n" +
                    "        <!-- margin-top: 线程信息所在高度 +60px -->\n" +
                    "        <div class=\"progress progress-striped\"\n" +
                    "            style=\"width: 70%;position: absolute;margin-left: 15%;margin-top: " + progressBarMarginTop.get() + "px;height: 35px;\">\n" +
                    "            <div class=\"progress-bar progress-bar-info\" role=\"progressbar\" aria-valuenow=\"60\" aria-valuemin=\"10\"\n" +
                    "                aria-valuemax=\"100\" style=\"width: " + result + "%;line-height: 35px;font-size: 15px;\">\n" +
                    progress + " / " + total + "\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "        <!-- margin-top: 同进度条 -->\n" +
                    "        <div style=\"position: absolute;margin-left: 90%;margin-top: " + progressBarMarginTop.get() + "px;\">\n" +
                    "            <button type=\"button\" class=\"btn btn-danger\" th:onclick=\"tryStop('" + key + "')\">取消</button>\n" +
                    "        </div>\n" +
                    "    </div>");
            threadDetailMarginTop.addAndGet(80);
            progressBarMarginTop.set(threadDetailMarginTop.get() + 60);
            msg.set((String) value.getOrDefault("msg", ""));
        });
        progressBarMarginTop.set(progressBarMarginTop.get());
        htmlContent.append("<!--  margin-top: 最后取消按钮所在高度 +50px -->\n" +
                "<a class=\"list-group-item\"\n" +
                "   style=\"width: 70%;position: absolute;margin-left: 15%;margin-top: " + progressBarMarginTop + "px;font-size: 15px;\">\n" +
                "    <p class=\"list-group-item-text\">\n" +
                msg.get() +
                "    </p>\n" +
                "</a>");
        modelAndView.addObject("htmlContent", htmlContent.toString());
        modelAndView.addObject("flush", flush.get());
        return modelAndView;
    }

    @GetMapping("/clearMessage")
    @ResponseBody
    public String clearMessage() {
        threadMap.entrySet().stream().filter(map -> !StringUtils.equals(map.getKey(), "index")).forEach(map -> {
            Map<String, Object> value = (Map<String, Object>) map.getValue();
            value.put("msg", "");
            threadMap.put(map.getKey(), value);
        });
        return "清理消息成功";
    }

    static class NameTreadFactory implements ThreadFactory {
        private final String threadName;

        NameTreadFactory(String threadName) {
            this.threadName = threadName;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, this.threadName);
            log.info(t.getName() + " has been created");
            return t;
        }
    }

    enum ConditionType {
        INPUT,
        SELECT
    }

    static class Condition {
        String conditionName;
        ConditionType conditionType;

        String getConditionName() {
            return conditionName;
        }

        void setConditionName(String conditionName) {
            this.conditionName = conditionName;
        }

        ConditionType getConditionType() {
            return conditionType;
        }

        void setConditionType(ConditionType conditionType) {
            this.conditionType = conditionType;
        }
    }

    private Condition inputCondition(String inputConditionName) {
        Condition condition = new Condition();
        condition.setConditionName(inputConditionName);
        condition.setConditionType(ConditionType.INPUT);
        return condition;
    }

    static class TestExecutionTool implements Runnable {

        private static String currentThreadName;

        private static ThreadLocal<String> msgLocal = new ThreadLocal<>();

        private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        private synchronized Map<String, Object> getCurrentThreadMap() {
            threadMap.computeIfAbsent(currentThreadName, k -> Maps.newConcurrentMap());
            return (Map<String, Object>) threadMap.get(currentThreadName);
        }

        public synchronized void setTotal(int total) {
            getCurrentThreadMap().put("total", total);
        }

        public synchronized int getTotal() {
            return (int) getCurrentThreadMap().getOrDefault("total", 0);
        }

        public synchronized void setProgress(int progress) {
            getCurrentThreadMap().put("progress", progress);
        }

        public synchronized int getProgress() {
            return (int) getCurrentThreadMap().getOrDefault("progress", 0);
        }

        private synchronized void println(String msg) {
            msgLocal.set(msgLocal.get() + msg + "<br/>");
            getCurrentThreadMap().put("msg", msgLocal.get());
        }

        synchronized void clearMessage() {
            msgLocal.set("");
            Map<String, Object> threadDetails = getCurrentThreadMap();
            threadDetails.put("msg", "");
            threadMap.put(currentThreadName, threadDetails);
        }

        synchronized void  tryStop() {
            Map<String, Object> threadDetails = getCurrentThreadMap();
            threadDetails.put("tryStop", true);
            threadDetails.put("endTime", sdf.format(new Date()));
            threadMap.put(currentThreadName, threadDetails);
        }

        private synchronized Boolean getTryStop() {
            return (Boolean) getCurrentThreadMap().getOrDefault("tryStop", false);
        }

        TestExecutionTool(String threadName) {
            currentThreadName = threadName;
            Map<String, Object> threadDetails = getCurrentThreadMap();
            threadDetails.put("index", threadMap.get("index"));
            threadDetails.put("startTime", sdf.format(new Date()));
            threadMap.put(currentThreadName, threadDetails);
        }

        @Override
        public void run() {
            synchronized (this) {
                this.clearMessage();
                setTotal(20);
                for (int i = 1; i <= 20; i++) {
                    try {
                        if (Boolean.TRUE.equals(getTryStop())) {
                            System.out.println("停止成功！");
                            break;
                        }
                        setProgress(i);
                        println("当前" + sdf.format(new Date()));
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println(msgLocal.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tryStop();
            }
        }
    }
}
