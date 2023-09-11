package vasilkov.labbpls2.service.mailUtils;


import lombok.Data;

@Data
public class MessageModel {
    private String message;
    private String routingKey; //пусть у данной лабы он будет один...
}