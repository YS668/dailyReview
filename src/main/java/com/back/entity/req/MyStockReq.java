package com.back.entity.req;

import javax.xml.ws.soap.Addressing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyStockReq {

    private Long uid;
    private String groupName;
    private String stockName;
    private String newGroup;
    private String note;
}
