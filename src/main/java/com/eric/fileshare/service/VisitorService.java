package com.eric.fileshare.service;

import com.eric.fileshare.dao.IVisitorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitorService implements IVisitorService{

    private IVisitorDAO visitorDAO;

    @Autowired
    public  VisitorService(IVisitorDAO visitorDAO) {
        this.visitorDAO = visitorDAO;
    }

    @Override
    public long getBalance(String ip) {
        return 1 * 1024 * 1024* 1024 - visitorDAO.getOccupiedSpaceByIp(ip);
    }
}
