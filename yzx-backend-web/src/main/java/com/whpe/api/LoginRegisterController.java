package com.whpe.api;

import com.whpe.bean.Result;
import com.whpe.controller.CommonController;
import org.springframework.stereotype.Controller;

@Controller
public class LoginRegisterController extends CommonController{

    public Result doLogin(){


        return new Result(true, "µÇÂ½³É¹¦");
    }

}
