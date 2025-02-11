package com.void2.careermanagement.controller;

import com.void2.careermanagement.dao.CompanyAccountDao;
import com.void2.careermanagement.dto.CompanyDto;
import com.void2.careermanagement.service.CompanyAccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company/account")
public class CompanyAccountApiController {

    private final CompanyAccountDao companyAccountDao;
    private final CompanyAccountService companyAccountService;

    public CompanyAccountApiController(CompanyAccountDao companyAccountDao, CompanyAccountService companyAccountService) {
        this.companyAccountDao = companyAccountDao;
        this.companyAccountService = companyAccountService;
    }

    @PostMapping("/login")
    public String userLogin(@RequestBody CompanyDto companyDto, HttpSession session) {

        CompanyDto sessionCompany = companyAccountDao.loginCompanySelect(companyDto);
        if (sessionCompany != null) {
            session.setAttribute("user", sessionCompany);
            session.setAttribute("userType", "C");
            String prevPage = session.getAttribute("prevPage") == null ? "/" : (String) session.getAttribute("prevPage");
            if (!prevPage.equals("/")) {
                session.removeAttribute("prevPage"); // 세션에서 제거
            }
            return prevPage;
        } else {
            return "false";
        }
    }

    @PostMapping("/regist")
    public boolean userRegister(@RequestBody CompanyDto companyDto) {
        return companyAccountDao.companyInsert(companyDto) != 0;
    }

    @GetMapping("/check-id/{companyId}")
    public int checkId(@PathVariable String companyId) {
        return companyAccountDao.findCompanyIdSelect(companyId);
    }

    @PutMapping("/update")
    public boolean userUpdate(@RequestBody CompanyDto companyDto, HttpSession session) {
        return companyAccountService.updateCompany(companyDto, session);
    }

    @DeleteMapping("/delete/{companyId}")
    public boolean userDelete(@PathVariable String companyId, HttpSession session) {
        return companyAccountService.deleteCompany(companyId, session);
    }

    @PostMapping("/session-edit")
    public boolean sessionEdit(HttpSession session) {
        CompanyDto sessionUser = (CompanyDto) session.getAttribute("user");
        if (sessionUser != null) {
            session.setAttribute("user", companyAccountDao.sessionSelect(sessionUser.getCompanyId()));
            return true;
        } else {
            return false;
        }
    }
}
