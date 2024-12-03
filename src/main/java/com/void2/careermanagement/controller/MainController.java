package com.void2.careermanagement.controller;

import com.void2.careermanagement.dto.UserDto;
import com.void2.careermanagement.response.CompanyResponseDto;
import com.void2.careermanagement.service.CompanyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created on 2024-12-02 by 안제연
 */

@Controller
public class MainController {
    private final CompanyService companyService;
    @Autowired
    public MainController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping("/")
    public String root(Model model, HttpSession session) {
        // 세션 null일시 만든 임시 경로
        // todo 비회원페이지 생성시 여기다 경로 적으세요
        if(session.getAttribute("user") == null) {
            return "/company/company-main";
        }

        String userType = (String) session.getAttribute("userType");
        System.out.println(userType);
        if (userType.equals("C")) {
            return "/company/company-main";
        } else {
            if (userType.equals("U")) {
                UserDto sessionUser = (UserDto) session.getAttribute("user");
                System.out.println("main");
                if (sessionUser != null) {
                    System.out.println(sessionUser.getUserId());
                    //관심기업 공고 리스트 빈 여부 확인
                    String isEmpty = "Empty";
                    //관심기업 공고 리스트
                    List<CompanyResponseDto> lList = companyService.getLikeCompanyList(sessionUser.getUserId());
                    if (!lList.isEmpty()) {
                        for (CompanyResponseDto l : lList) System.out.println(l);
                        isEmpty = "notEmpty";
                        model.addAttribute("lList", lList);
                    }
                    model.addAttribute("isEmpty", isEmpty);
                }
            }
            //평점 높은 회사 리스트
            List<CompanyResponseDto> cList = companyService.getHighRatingCompanyList();
            //for(CompanyResponseDto c : cList) System.out.println(c);
            //채용임박 공고 리스트
            List<CompanyResponseDto> eList = companyService.getFastDeadLineList();
            //(CompanyResponseDto e : eList) System.out.println(e);

            model.addAttribute("cList", cList);
            model.addAttribute("eList", eList);
            return "main";
        }
    }
}
