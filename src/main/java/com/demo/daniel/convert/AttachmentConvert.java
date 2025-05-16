package com.demo.daniel.convert;

import com.demo.daniel.model.entity.Attachment;
import com.demo.daniel.model.vo.AttachmentVO;
import org.springframework.beans.BeanUtils;

public class AttachmentConvert {

    private AttachmentConvert() {
    }

    public static AttachmentVO convertToVO(Attachment attachment) {
        AttachmentVO attachmentVO = new AttachmentVO();
        BeanUtils.copyProperties(attachment, attachmentVO);
        return attachmentVO;
    }
}
