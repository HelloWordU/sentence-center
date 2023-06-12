package com.ruizhi.tech.sentencecenter.service;


import com.alibaba.fastjson.JSON;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.ruizhi.tech.sentencecenter.pojo.vo.request.AddSentenceRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.ruizhi.tech.sentencecenter.SentenceCenterApplication.class)
public class SentenceServiceTest {

    @Resource
    private SentenceService sentenceService;

    @Test
    public void add() {
        String info = "随着出境游市场的回暖，出境旅行产品的预定量大幅增长。随着五一小长假的临近，东南亚和日本等地区成为了炙手可热的旅游目的地。如果您正在考虑旅行计划，那么您需要考虑一个稳定的网络才能够享受出境旅游的乐趣。华为天际通现在送出10万张全球上网流量券，让您轻松畅游世界。打开天际通，点击“出发！去旅行”活动页，领取天际通出行权益，抵达目的地一键启用即可畅享高速网络。福利先到先得，赶紧去领取吧！\n" +
                "\n" +
                "华为手机自带APP“天际通”可以解决用户出境上网难题。您不需要更换卡片，也不需要繁琐的准备。一键解锁全球超过100个目的地的流量套餐，包括流量或天数计费的不同套餐，以满足用户不同的出行需求。以东南亚热门旅游地为例，您可以选择仅需28元的1GB流量套餐或42元的三日行东南亚通用流量套餐。算下来，每天只需花费一杯奶茶的价格就可以拥有高速网络。此外，天际通还支持试用后购买，并且在境外无网络的情况下也能购买和启用套餐，省心省钱。\n" +
                "\n" +
                "天际通还为常用App带来专线加速服务，让您在境外也能够流畅地使用这些应用。当多人出行时，您可以打开热点与亲友共享流量，并且流量使用情况可以随时在APP中查看，使您可以轻松掌握您的使用情况。\n" +
                "\n" +
                "除了便捷、可靠、套餐多样化的境外上网服务外，APP中还提供机酒查询订购、签证办理、热门景点介绍等“旅游必备技能”。不需要再东查西查，天际通将帮助您搞定所有问题。携带天际通，一起出发，去旅游吧！";
        String[] contents = info.split("\n");
        for (String content : contents) {
            if (content.length() > 0) {
                AddSentenceRequest request = new AddSentenceRequest();
                request.setContent(content);
                sentenceService.add(request);
            }
        }
    }

    @Test
    public void getSentenceKeys() {
        String info = "随着出境游市场的回暖，出境旅行产品的预定量大幅增长。随着五一小长假的临近，东南亚和日本等地区成为了炙手可热的旅游目的地。如果您正在考虑旅行计划，那么您需要考虑一个稳定的网络才能够享受出境旅游的乐趣。华为天际通现在送出10万张全球上网流量券，让您轻松畅游世界。打开天际通，点击“出发！去旅行”活动页，领取天际通出行权益，抵达目的地一键启用即可畅享高速网络。福利先到先得，赶紧去领取吧！\n" +
                "\n" +
                "华为手机自带APP“天际通”可以解决用户出境上网难题。您不需要更换卡片，也不需要繁琐的准备。一键解锁全球超过100个目的地的流量套餐，包括流量或天数计费的不同套餐，以满足用户不同的出行需求。以东南亚热门旅游地为例，您可以选择仅需28元的1GB流量套餐或42元的三日行东南亚通用流量套餐。算下来，每天只需花费一杯奶茶的价格就可以拥有高速网络。此外，天际通还支持试用后购买，并且在境外无网络的情况下也能购买和启用套餐，省心省钱。\n" +
                "\n" +
                "天际通还为常用App带来专线加速服务，让您在境外也能够流畅地使用这些应用。当多人出行时，您可以打开热点与亲友共享流量，并且流量使用情况可以随时在APP中查看，使您可以轻松掌握您的使用情况。\n" +
                "\n" +
                "除了便捷、可靠、套餐多样化的境外上网服务外，APP中还提供机酒查询订购、签证办理、热门景点介绍等“旅游必备技能”。不需要再东查西查，天际通将帮助您搞定所有问题。携带天际通，一起出发，去旅游吧！";
        List<String> sentenceKeys = sentenceService.getSentenceKeys(info);
        System.out.println(JSON.toJSONString(sentenceKeys));
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> process = segmenter.process(info, JiebaSegmenter.SegMode.SEARCH);
        List<String> searchKey = process.stream().map(i->i.word).distinct().collect(Collectors.toList());
        System.out.println(JSON.toJSONString(process));
        //  String[] contents = info.split("\n");
//        for (String content : contents) {
//            if (content.length() > 0) {
//                AddSentenceRequest request = new AddSentenceRequest();
//                request.setContent(content);
//                sentenceService.add(request);
//            }
//        }
    }
}