package org.hy.microservice.common.demo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;




/**
 * 上传样例Demo的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2026-03-12
 * @version     v1.0
 */
@Controller
@RequestMapping(value="demoUpload" ,name="上传演示")
public class DemoUploadController
{
    
    /**
     * excel文件上传
     *
     * @param request
     * @param i_Response
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> upload(HttpServletRequest i_Request, HttpServletResponse i_Response) 
    {
        
        Map<String, Object> result = new HashMap<String, Object>();
        
        try
        {
            // 方案1：安全强转（适配3.5，兼容2.x）
            MultipartHttpServletRequest v_MultipartRequest;
            if ( i_Request instanceof MultipartHttpServletRequest )
            {
                v_MultipartRequest = (MultipartHttpServletRequest) i_Request;
            }
            else
            {
                // 手动触发解析（3.5 延迟解析时兜底）
                v_MultipartRequest = new StandardMultipartHttpServletRequest(i_Request);
            }
            // 获取文件Map（和2.x用法一致）
            MultiValueMap<String ,MultipartFile> v_FileMap = v_MultipartRequest.getMultiFileMap();
            // 示例：遍历文件（验证是否获取成功）
            for (String key : v_FileMap.keySet())
            {
                for (MultipartFile file : v_FileMap.get(key))
                {
                    System.out.println("上传文件：" + key + " -> " + file.getOriginalFilename());
                }
            }
            result.put("code" ,200);
            result.put("msg" ,"文件解析成功");
            result.put("fileCount" ,v_FileMap.size());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result.put("code" ,500);
            result.put("msg" ,"文件解析失败：" + e.getMessage());
        }
        
        
        /*
        response.setCharacterEncoding("utf-8");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, String[]> parameterMap = multipartRequest.getParameterMap();
        String fileType = parameterMap.get("fileType")[0];
        String userName = "";
        String realPath = request.getSession().getServletContext().getRealPath("/");
        String uploadPath = realPath + "upload" + File.separator + userName + File.separator;
        File destDir = new File(uploadPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        //文件遍历上传
        
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile mf = entity.getValue();
            InputStream in = null;
            OutputStream out = null;
            try {
                in = mf.getInputStream();
                // 上传文件名、后缀名
                String oldFileName = mf.getOriginalFilename();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String nowTime = sdf.format(new Date());

                String newFileName = fileType + "_" + nowTime + oldFileName.substring(oldFileName.lastIndexOf("."));
                String fileFullPath = uploadPath + newFileName;
                File saveFile = new File(fileFullPath);
                out = new FileOutputStream(saveFile);
                IOUtils.copy(in, out);
                RTemplate template = (RTemplate) XJava.getObject("basicDataOfPartProcessingTasks");
                List<MachineDeptDispatchManager> processClassList = (List<MachineDeptDispatchManager>) ReportHelp.toJava(template, fileFullPath);
                if (processClassList != null && processClassList.size() > 0) {
                    //根据计划编号查询
                    List<MachineDeptDispatchManager> machineDeptDispatchManagerList = service.queryByPlanCode(processClassList);
                    if (machineDeptDispatchManagerList != null && machineDeptDispatchManagerList.size() > 0) {
                        String planCodes = machineDeptDispatchManagerList.stream()
                                .map(MachineDeptDispatchManager::getPlanCode)
                                .collect(Collectors.joining("、"));
                        result.put("success", false);
                        result.put("msg", "导入失败," + planCodes + "已经存在");
                    } else {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        // 将processClassList中startTime和endTime更换格式为yyyy-MM-dd
                        processClassList.stream().forEach(p -> {
                            try {
                                p.setStartTime(format.format(format.parse(p.getStartTime())));
                                p.setEndTime(format.format(format.parse(p.getEndTime())));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        });
                        Map<String, MachineDeptDispatchManager> filteredMap = processClassList.stream()
                                .collect(Collectors.toMap(
                                        MachineDeptDispatchManager::getPlanCode,
                                        manager -> manager,
                                        (existing, replacement) -> existing
                                ));
                        List<MachineDeptDispatchManager> filteredList = filteredMap.values().stream()
                                .collect(Collectors.toList());
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formattedDateTime = currentDateTime.format(formatter);
                        //添加到parts_process_manager_main
                        filteredList.forEach(machineDeptDispatchManager -> {
                            machineDeptDispatchManager.setCompanyCode(CommonUtils.getUser(request).getCompanyCode());
                            machineDeptDispatchManager.setInputQty(0);
                            machineDeptDispatchManager.setSavePerson(CommonUtils.getUser(request).getNickName());
                            machineDeptDispatchManager.setSaveTime(formattedDateTime);
                        });
                        service.insetManagerMain(filteredList);
                        //添加到parts_processInfor表中
                        processClassList.forEach(machineDeptDispatchManager -> {
                            machineDeptDispatchManager.setCompanyCode(CommonUtils.getUser(request).getCompanyCode());
                            machineDeptDispatchManager.setGiveUpFlag("0");
                            machineDeptDispatchManager.setSynchronizationTime(formattedDateTime);
                        });
                        service.insetDispatchManager(processClassList);
                        //添加数据到order_bom_item_plan表中
                        List<MachineDeptDispatchManager> arrayList = new ArrayList<>(filteredMap.values());
                        String companyCode = CommonUtils.getUser(request).getCompanyCode();
                        List<Department> sysOrgs = departmentService.queryAlldepartCode(companyCode);
                        arrayList.forEach(machineDeptDispatchManager -> {
                            sysOrgs.forEach(department -> {
                                if (machineDeptDispatchManager.getDepartCode().equals(department.getDepartCode())) {
                                    machineDeptDispatchManager.setDepartName(department.getDepartName());
                                }
                            });
                            machineDeptDispatchManager.setSaveTime(formattedDateTime);
                            machineDeptDispatchManager.setSavePerson(CommonUtils.getUser(request).getNickName());
                            machineDeptDispatchManager.setCompanyCode(CommonUtils.getUser(request).getCompanyCode());
                        });
                        service.insetBomItem(arrayList);
                    }

                    result.put("success", true);
                    result.put("msg", "导入成功");
                }
            } catch (Exception e) {
                log.error("upload(HttpServletRequest, HttpServletResponse)", e);
                result.put("success", false);
                result.put("msg", "生成加工任务失败，请检查文件以及数据是否正确。");
            } finally {
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
            }
        }
        */
        return result;
    }
    
}
