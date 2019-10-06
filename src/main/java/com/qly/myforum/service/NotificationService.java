package com.qly.myforum.service;

import com.qly.myforum.dto.NotificationDTO;
import com.qly.myforum.dto.PaginationDTO;
import com.qly.myforum.enums.NotificationStatusEnum;
import com.qly.myforum.enums.NotificationTypeEnum;
import com.qly.myforum.exception.CustomizeErrorCode;
import com.qly.myforum.exception.CustomizeException;
import com.qly.myforum.mapper.NotificationMapper;
import com.qly.myforum.pojo.Notification;
import com.qly.myforum.pojo.NotificationExample;
import com.qly.myforum.pojo.Question;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    @Autowired
    NotificationMapper notificationMapper;

    public PaginationDTO list(Long id, Integer page, Integer size) {
        PaginationDTO<NotificationDTO>paginationDTO = new PaginationDTO<NotificationDTO>();

        Integer totalPage;
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        int totalCount = notificationMapper.selectByExample(notificationExample).size();
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage, page);
        Integer offset = size * (page - 1);
        List<Notification> notifications = notificationMapper.selectByPageAndId(id, offset, size);

        if (notifications.size() == 0) {
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOS = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification, notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);
        return paginationDTO;
    }

    public Notification read(Long notificationId, Long id) {
        Notification notification = notificationMapper.selectByPrimaryKey(notificationId);
        if(notification==null){
            throw  new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(),id)){
            throw  new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        return notification;
    }
}

