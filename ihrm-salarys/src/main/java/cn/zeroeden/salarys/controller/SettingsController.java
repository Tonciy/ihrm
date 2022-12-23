package cn.zeroeden.salarys.controller;

import cn.zeroeden.controller.BaseController;
import cn.zeroeden.domain.salarys.Settings;
import cn.zeroeden.entity.Result;
import cn.zeroeden.entity.ResultCode;
import cn.zeroeden.salarys.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/salarys")
public class SettingsController extends BaseController {

	@Autowired
	private SettingsService settingsService;

	/**
	 * 获取企业计薪及津贴设置
	 */
	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public Result getSettings() throws Exception {
		Settings settings = settingsService.findById(companyId);
		return new Result(ResultCode.SUCCESS, settings);
	}

	/**
	 * 保存企业计薪及津贴设置
	 */
	@RequestMapping(value = "/settings", method = RequestMethod.POST)
	public Result saveSettings(@RequestBody Settings settings) throws Exception {
		settings.setCompanyId(companyId);
		settingsService.saveByMy(settings);
		return new Result(ResultCode.SUCCESS);
	}
}
