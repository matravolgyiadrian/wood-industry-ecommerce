package org.thesis.woodindustryecommerce.services.implementations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thesis.woodindustryecommerce.model.CartItem;
import org.thesis.woodindustryecommerce.model.Order;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendProductReorderEmail(String productName, int reorderedAmount){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("admin@admin");
        message.setSubject("Reordered product");
        message.setText("The "+productName+" isn't in stock. It has been automatically reordered to have "+reorderedAmount + "pcs in stock!");
    }

    public void sendTemplateEmail(Order order) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariable("shopping_cart", order.getProducts());
        context.setVariable("customer", order.getCustomer());
        context.setVariable("total_price", order.getTotalPrice());
        context.setVariable("discount", calculateTotalPrice(order.getProducts()) - order.getTotalPrice());

        String html = templateEngine.process("email", context);
        helper.setTo(order.getEmail());
        helper.setText(html, true);
        helper.setSubject("Successful order");
        mailSender.send(message);
        log.debug("Send template email to: "+order.getEmail());
    }

    public void sendOrderStatusChangedEmail(Order order) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariable("shopping_cart", order.getProducts());
        context.setVariable("customer", order.getCustomer());
        context.setVariable("total_price", order.getTotalPrice());
        context.setVariable("discount", calculateTotalPrice(order.getProducts()) - order.getTotalPrice());
        context.setVariable("status", order.getStatus().ordinal());

        String html = templateEngine.process("email", context);
        helper.setTo(order.getEmail());
        helper.setText(html, true);
        helper.setSubject("Update to your delivery");
        mailSender.send(message);
        log.debug("Send order status changed email to: "+order.getEmail());
    }

    private double calculateTotalPrice(List<CartItem> cart) {
        int sum = 0;
        for (CartItem item : cart) {
            sum += item.getTotalPrice();
        }
        return sum;
    }
}
