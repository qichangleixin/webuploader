	@RequestMapping(name = "报告总结附件上传", value = "/summaryUpload")
	@ResponseBody
	public SysFile summaryUpload(HttpServletRequest request) throws Exception {
		String uploadpath = Constant.ASSESSMENT;
		String businessId = request.getParameter("businessId");
        SysFile sysFileResult = null;
		try {
			File savePath = new File(sysConfigInfo.getGplotSavePath()+uploadpath);
    		if (!savePath.exists()) {
    			savePath.mkdirs();	
    		}
    		List<SysFile> sysFileList = new ArrayList<>();
            MultipartHttpServletRequest Murequest = (MultipartHttpServletRequest)request;
            Map<String, MultipartFile> files = Murequest.getFileMap();//得到文件map对象
            if(files!=null&&!files.isEmpty()){
                //循环获取file数组中得文件  
                for(MultipartFile file:files.values()){
        			String fileName = fileUpload(request, file, sysConfigInfo.getGplotSavePath()+uploadpath);//文件上传
        			String filePath = uploadpath+fileName; //路径+新命名（uuid+后缀名）
        			SysFile sysFile = new SysFile();
        			sysFile.setId(Common.getUUID());
        			sysFile.setBusinessId(businessId);
        			sysFile.setFileName(file.getOriginalFilename());
        			sysFile.setFilePath(filePath);
        			sysFile.setSize(file.getSize());
        			sysFile.setCreatTime(new Date());
    				String suffix  = file.getOriginalFilename().split("\\.")[file.getOriginalFilename().split("\\.").length-1];
        			sysFile.setExt(suffix);
        			sysFileList.add(sysFile);
        			sysFileResult = sysFile;
                }
            }  
            sysFileService.batchInsert(sysFileList);
        }catch (Exception e) {
        	logger.error("【文件上传失败】"+e.getMessage()); 
        }
		return sysFileResult;
	}
