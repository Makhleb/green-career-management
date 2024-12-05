package com.void2.careermanagement.controller;

import com.void2.careermanagement.dao.ApplyDao;
import com.void2.careermanagement.dao.LikeDao;
import com.void2.careermanagement.dao.MyPageDao;
import com.void2.careermanagement.dto.CompanyDto;
import com.void2.careermanagement.dto.UserDto;
import com.void2.careermanagement.dto.response.*;
import com.void2.careermanagement.service.ApplyService;
import com.void2.careermanagement.service.JobPostService;
import com.void2.careermanagement.service.MyPageService;
import com.void2.careermanagement.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

/**
 * Created on 2024-12-04 by 최윤서
 */
@Controller
@RequestMapping("/mypage")
public class MyPageController {

    private final MyPageService myPageService;
    private final ApplyDao applyDao;
    private final LikeDao likeDao;
    private JobPostService jobPostService;
    private ApplyService applyService;
    private MyPageDao myPageDao;

    @Autowired
    public MyPageController(MyPageService myPageService, ApplyDao applyDao, JobPostService jobPostService, ApplyService applyService, MyPageDao myPageDao, LikeDao likeDao) {
        this.myPageService = myPageService;
        this.applyDao = applyDao;
        this.jobPostService = jobPostService;
        this.applyService = applyService;
        this.myPageDao = myPageDao;
        this.likeDao = likeDao;
    }


    public MyPageController(MyPageService myPageService, ApplyDao applyDao) {
        this.myPageService = myPageService;
        this.applyDao = applyDao;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (SessionUtil.sessionUserCheckRedirectLogin(session, request, response)) return null;
        String userType = session.getAttribute("userType").toString();
        String returnUrl = "";
//        Object sessionUser = session.getAttribute("user");

        String id = SessionUtil.getSessionUserId(session);
        if (userType.equals("U")) {
//            UserDto user = (UserDto) sessionUser;
//            String userId = user.getUserId(); // UserDto 객체의 userId 필드 접근

            List<ResumeResponseDto> resumeList = myPageService.MyPageResumeListById(id);
            List<MyPageScrapDto> scrapList = myPageService.MyPageScrapListByIdTop3(id);
            model.addAttribute("resumeList", resumeList);
            model.addAttribute("scrapList", scrapList);
            model.addAttribute("scrapSize", myPageDao.getCountScrapByUserId(id));
            returnUrl = "/mypage/user-mypage";
        } else if (userType.equals("C")) {
//            CompanyDto user = (CompanyDto) sessionUser;
//            String companyId = user.getCompanyId();

            // 제안서 리스트
            List<ProposalResponseDto> proposalList = myPageService.MyPageProposalListByCompanyIdTop3(id);
            model.addAttribute("proposalSize", myPageDao.getCountProposalByCompanyId(id));
            model.addAttribute("plist", proposalList);
            
            // 채용공고 리스트
            List<JobPostResponseDto> jobPostList = myPageService.MyPageJobPostListByCompanyIdTop3(id);
            model.addAttribute("jobPostSize", myPageDao.getCountJobPostByCompanyId(id));
            model.addAttribute("jobPostList", jobPostList);

            // 좋아요 구직자 리스트
            List<LikeResponseDto> likeList = likeDao.getLikeListByCompanyIdTop3(SessionUtil.getSessionUserId(session));
            model.addAttribute("likeList", likeList);

            returnUrl = "/mypage/company-mypage";
        }
        return returnUrl;
    }


    @GetMapping("/apply")
    public String apply(HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (SessionUtil.sessionUserCheckRedirectLogin(session, request, response)) return null;

        String userType = session.getAttribute("userType").toString();
        String returnPage = "";

        String id = SessionUtil.getSessionUserId(session);

        if (userType.equals("U")) {
            List<ApplyResponseDto> applyList = applyDao.getApplyListByUserId(id);

            model.addAttribute("applyList", applyList);
            returnPage = "/mypage/user-apply";
        } else if (userType.equals("C")) {
//
//            CompanyDto user = (CompanyDto) sessionUser;
//            String companyId = user.getCompanyId();
//            List<JobPostResponseDto> jobPostList = jobPostService.getJobPostListByCompanyId(companyId);
//            System.out.println(jobPostList);
//            model.addAttribute("jobPostList", jobPostList);
//            returnPage = "/mypage/company-apply";
        }
        return returnPage;
    }

    @GetMapping("/apply/{jobPostNo}")
    public String apply(@PathVariable int jobPostNo, HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (SessionUtil.sessionUserCheckRedirectLogin(session, request, response)) return null;
        String userType = session.getAttribute("userType").toString();
        String returnPage = "";

        if (userType.equals("C")) {
            List<ApplyResponseDto> applyList = applyService.getApplyListByJobPostNo(jobPostNo);
            System.out.println(applyList);
            model.addAttribute("applyList", applyList);
            returnPage = "/mypage/company-apply";
        }
        return returnPage;
    }

    @GetMapping("/job-post")
    public String jobPost(HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (SessionUtil.sessionUserCheckRedirectLogin(session, request, response)) return null;

        String userType = session.getAttribute("userType").toString();
        String returnPage = "";

        String id = SessionUtil.getSessionUserId(session);

        if (userType.equals("U")) {

            returnPage = "/mypage/user-mypage";

        } else if (userType.equals("C")) {

            List<JobPostResponseDto> jobPostList = jobPostService.getJobPostListByCompanyId(id);
            System.out.println(jobPostList);
            model.addAttribute("jobPostList", jobPostList);
            returnPage = "/mypage/company-job-post";

        }
        return returnPage;
    }

    @GetMapping("/company-interest-user")
    public String userInterestCompany(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(SessionUtil.sessionUserCheckRedirectLogin(session, request, response)) return null;

        List<LikeResponseDto> likeList = likeDao.getLikeListByCompanyId(SessionUtil.getSessionUserId(session));

        model.addAttribute("likeList", likeList);
        return "/mypage/user-interest-company";
    }

}
