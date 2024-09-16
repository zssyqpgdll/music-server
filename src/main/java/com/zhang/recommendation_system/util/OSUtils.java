package com.zhang.recommendation_system.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 操作系统工具类
 * @author modi
 *
 */
public class OSUtils {
//	String osName = System.getProperty("os.name");
//	if (osName.toLowerCase().startsWith("windows")) {
//        cpuRatio = this.getCpuRatioForWindows();
//    }

    private static final int CPUTIME = 500;//采集频率
    private static final int FAULTLENGTH = 10;

    /**
     * 获取cpu使用率
     * <p>1表示100%
     * @return
     */
    public static double getCpuRatio(){
        double cpuRatio = -1;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().startsWith("windows")) {
            cpuRatio = getCpuRatioForWindows();
        }else{
            cpuRatio = getCpuRatioForLinux();
        }
//        System.out.println("系统负载率：" + cpuRatio);
        return cpuRatio;
    }

    // 获得cpu使用率
    public static double getCpuRatioForWindows() {

        try {
            String procCmd = System.getenv("windir") + "\\system32\\wbem\\wmic.exe process get Caption, KernelModeTime, UserModeTime";
//            		System.getenv("windir") + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // 取进程信息
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));

            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                return Double.valueOf(busytime)/(busytime + idletime);
            } else {
                return   0;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return  -1;
        }
    }

    private static long[] readCpu(final Process proc) {
        long[] retn = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH) {
                return null;

            }
//            Caption, KernelModeTime, UserModeTime

            int capidx = line.indexOf("Caption");
            int kmtidx = line.indexOf("KernelModeTime");
            int umtidx = line.indexOf("UserModeTime");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < umtidx) {
                    continue;

                }
                // 字段出现顺序：Caption, KernelModeTime, UserModeTime
                String caption = Bytes.substring(line, capidx, kmtidx - 1).trim();
                if (caption.toLowerCase().indexOf("wmic.exe") >= 0) {
                    continue;
                }
                String s1 = Bytes.substring(line, kmtidx, umtidx - 1).trim();
                String s2 = Bytes.substring(line, umtidx, line.length()-1).trim();
                if (caption.equals("System Idle Process") || caption.equals("System")) {

                    if (s1.length() > 0)

                        idletime += Long.valueOf(s1).longValue();
                    if (s2.length() > 0)
                        idletime += Long.valueOf(s2).longValue();
                    continue;
                }
                if (s1.length() > 0)

                    kneltime += Long.valueOf(s1).longValue();
                if (s2.length() > 0)
                    usertime += Long.valueOf(s2).longValue();
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;

    }
    static class Bytes {
        public static String substring(String src, int start_idx, int end_idx) {
            byte[] b = src.getBytes();
            String tgt = "";
            for (int i = start_idx; i <= end_idx; i++) {
                tgt += (char) b[i];
            }

            return tgt;
        }
    }

    /////////////////linux////////////////////
    /**
     * 功能：获取Linux系统cpu使用率
     * */
    public static double getCpuRatioForLinux() {
        try {
            Map<?, ?> map1 = OSUtils.cpuinfo();
            Thread.sleep(CPUTIME);
            Map<?, ?> map2 = OSUtils.cpuinfo();

            long user1 = Long.parseLong(map1.get("user").toString());
            long nice1 = Long.parseLong(map1.get("nice").toString());
            long system1 = Long.parseLong(map1.get("system").toString());
            long idle1 = Long.parseLong(map1.get("idle").toString());

            long user2 = Long.parseLong(map2.get("user").toString());
            long nice2 = Long.parseLong(map2.get("nice").toString());
            long system2 = Long.parseLong(map2.get("system").toString());
            long idle2 = Long.parseLong(map2.get("idle").toString());

            long total1 = user1 + system1 + nice1;
            long total2 = user2 + system2 + nice2;
            double total = total2 - total1;

            long totalIdle1 = user1 + nice1 + system1 + idle1;
            long totalIdle2 = user2 + nice2 + system2 + idle2;
            double totalidle = totalIdle2 - totalIdle1;

            return total / totalidle;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 功能：CPU使用信息
     * */
    public static Map<?, ?> cpuinfo() {
        InputStreamReader inputs = null;
        BufferedReader buffer = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            inputs = new InputStreamReader(new FileInputStream("/proc/stat"));
            buffer = new BufferedReader(inputs);
            String line = "";
            while (true) {
                line = buffer.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("cpu")) {
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    List<String> temp = new ArrayList<String>();
                    while (tokenizer.hasMoreElements()) {
                        String value = tokenizer.nextToken();
                        temp.add(value);
                    }
                    map.put("user", temp.get(1));
                    map.put("nice", temp.get(2));
                    map.put("system", temp.get(3));
                    map.put("idle", temp.get(4));
                    map.put("iowait", temp.get(5));
                    map.put("irq", temp.get(6));
                    map.put("softirq", temp.get(7));
                    map.put("stealstolen", temp.get(8));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
                inputs.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return map;
    }
}