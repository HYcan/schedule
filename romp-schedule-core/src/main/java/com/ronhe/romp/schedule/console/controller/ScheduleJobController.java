package com.ronhe.romp.schedule.console.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ronhe.romp.schedule.console.model.ScheduleJobModel;
import com.ronhe.romp.schedule.console.service.ScheduleJobService;
import com.ronhe.romp.schedule.console.util.validation.AnnotationValidator;
import com.ronhe.romp.schedule.console.util.validation.ValidateResult;
import com.ronhe.romp.schedule.dto.PageBodyDto;
import com.ronhe.romp.schedule.dto.ResponseBodyDto;

@Controller
@RequestMapping("/schedule/job")
public class ScheduleJobController {
	    private static final Logger logger = LoggerFactory.getLogger(ScheduleJobController.class);
	 
	    @Resource
		private ScheduleJobService scheduleJobService;

		@RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
	    @ResponseBody
		public ResponseBodyDto saveOrUpdate(ScheduleJobModel scheduleJobModel) {
			 ResponseBodyDto responseBodyDto = new ResponseBodyDto();
			 try {
				 ValidateResult validateResult =AnnotationValidator.validate(scheduleJobModel);
				 if(!validateResult.isValid()) {
						responseBodyDto.setSuccess(false);
						responseBodyDto.setMsg(validateResult.getMessage());
						return responseBodyDto;
				 }
				 responseBodyDto =scheduleJobService.saveOrUpdate(scheduleJobModel);
			 } catch (Exception e) {
				 responseBodyDto.setMsg(e.getMessage());
				 responseBodyDto.setSuccess(false);
				 logger.error(e.getMessage());
			 }
			 return responseBodyDto;
		}
		
		@RequestMapping(value = "getById/{id}", method = RequestMethod.GET)
	    @ResponseBody
		public ScheduleJobModel getById(@PathVariable("id")String id) {
			 ScheduleJobModel scheduleJobModel = null;
			 try {
				 scheduleJobModel =  scheduleJobService.getById(id);
			 } catch (Exception e) {
				 logger.error(e.getMessage()); 
			 }
			 return scheduleJobModel; 
		}
		
		@RequestMapping(value = "deleteById/{id}", method = RequestMethod.GET)
	    @ResponseBody
		public ResponseBodyDto deleteById(@PathVariable("id")String id) {
			 ResponseBodyDto responseBodyDto = new ResponseBodyDto();
			 try {
				 scheduleJobService.deleteById(id);
			 } catch (Exception e) {
				 responseBodyDto.setMsg(e.getMessage());
				 responseBodyDto.setSuccess(false);
				 logger.error(e.getMessage());
			 }
			 return responseBodyDto;
		}
		@RequestMapping(value = "getListPageByCondtion", method = RequestMethod.GET)
		@ResponseBody
		public PageBodyDto<ScheduleJobModel> getListPageByCondtion(ScheduleJobModel scheduleJobModel, PageBodyDto<ScheduleJobModel> pageBodyDtoMap) {
				try {
					PageBodyDto<ScheduleJobModel> pageBodyDto = scheduleJobService.getListPageByCondtion(scheduleJobModel, pageBodyDtoMap);
					return pageBodyDto;
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				return pageBodyDtoMap;
		}
	 
}
