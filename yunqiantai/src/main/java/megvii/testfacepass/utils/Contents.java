package megvii.testfacepass.utils;

import com.baidu.tts.client.TtsMode;

import java.util.Random;

import megvii.testfacepass.R;
import megvii.testfacepass.tts.util.OfflineResource;

public class Contents {

    //百度语音
    public String appId = "11644783";
    public String appKey = "knGksRFLoFZ2fsjZaMC8OoC7";
    public String secretKey = "IXn1yrFezEo55LMkzHBGuTs1zOkXr9P4";
    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    public TtsMode ttsMode = TtsMode.MIX;
    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
    // assets目录下bd_etts_speech_female.data为离线男声模型；bd_etts_speech_female.data为离线女声模型
    public String offlineVoice = OfflineResource.VOICE_FEMALE;
    // 主控制类，所有合成控制方法从这个类开始

    //
    public String authIP = "https://api-cn.faceplusplus.com";
    public String apiKey = "zIvtfbe_qPHpLZzmRAE-zVg7-EaVhKX2";
    public String apiSecret = "-H4Ik0iZ_5YTyw5NPT8LfnJREz_NCbo7";


    private int[] topIm = new int[]{R.drawable.sp1, R.drawable.sp2, R.drawable.sp3, R.drawable.sp4, R.drawable.sp5,
            R.drawable.sp6, R.drawable.sp7, R.drawable.sp8, R.drawable.sp9,};

    private String cocos[] = new String[]{"#93d778", "#e4e36c", "#fc9d9d", "#9dc8fc", "#fcd39d", "#d6aaff", "#aaf2ff"};

    private int[] qqIm = new int[]{R.drawable.qq3, R.drawable.
            qq5, R.drawable.qq6, R.drawable.qq7, R.drawable.qq8};

    private String texts[]=new String[]{"时间是一切财富中最宝贵的财富。","笨蛋自以为聪明，聪明人才知道自己是笨蛋。",
            "最甜美的是爱情，最苦涩的也是爱情。","大多数人想要改造这个世界，但却罕有人想改造自己。","人生最大的骄傲，就是在成年时实现童年的梦想。",
    "人生在世求名利难，求一份好心情也难，随遇而安更难。无论做任何事不要为了过份追求名利而破坏了自己的那份好心情。"};


    public String[] getCocos(){

        return cocos;
    }
    public int[] getTopIm(){

        return topIm;
    }
    public int[] getQQim(){

        return qqIm;
    }
    public String getTexts(){

        int min = 0;
        int max = 5;
        Random random = new Random();
        int num = random.nextInt(max) % (max - min + 1) + min;
        return texts[num];
    }

}
