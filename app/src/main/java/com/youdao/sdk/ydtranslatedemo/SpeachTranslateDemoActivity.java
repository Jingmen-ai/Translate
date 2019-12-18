package com.youdao.sdk.ydtranslatedemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.youdao.sdk.app.EncryptHelper;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.common.Constants;
import com.youdao.sdk.ydonlinetranslate.SpeechTranslateParameters;
import com.youdao.sdk.ydonlinetranslate.SpeechTranslate;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;
import com.youdao.sdk.ydtranslatedemo.utils.SwListDialog;
import com.youdao.sdk.ydtranslatedemo.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpeachTranslateDemoActivity extends Activity {

    private TextView resultText;
    private TextView filePathText;
    private Button toDetail;
    private File audioFile;
    TextView languageSelectFrom;

    TextView languageSelectTo;
    private Translate tr = null;
    ExtAudioRecorder recorder;
    SpeechTranslateParameters tps;
    Handler handler = new Handler();

    /**
     * 录音失败的提示
     */
    ExtAudioRecorder.RecorderListener listener = new ExtAudioRecorder.RecorderListener() {
        @Override
        public void recordFailed(int failRecorder) {
            if (failRecorder == 0) {
                Toast.makeText(SpeachTranslateDemoActivity.this, "录音失败，可能是没有给权限", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SpeachTranslateDemoActivity.this, "发生了未知错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voicerecognize_demo);

        resultText = (TextView) findViewById(R.id.shibietext);
        filePathText = (TextView) findViewById(R.id.filepath);
        toDetail = (Button) findViewById(R.id.detailBtn);
        languageSelectFrom = (TextView) findViewById(R.id.languageSelectFrom);
        languageSelectFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectFromLanguage(languageSelectFrom);
            }
        });
        languageSelectTo = (TextView) findViewById(R.id.languageSelectTo);


        languageSelectTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectToLanguage(languageSelectTo);
            }
        });
    }
    private void selectFromLanguage(final TextView languageSelect) {
        final String str[] = LanguageUtils.fromLangs;
        List<String> items = new ArrayList<String>();
        for (String s : str) {
            items.add(s);
        }

        SwListDialog exitDialog = new SwListDialog(SpeachTranslateDemoActivity.this,
                items);
        exitDialog.setItemListener(new SwListDialog.ItemListener() {

            @Override
            public void click(int position, String title) {

                //String language = languageSelect.getText().toString();
                languageSelect.setText(title);
                //String from = languageSelectFrom.getText().toString();
                //String to = languageSelectTo.getText().toString();

                //String lan = "中文";
//                if (!from.contains(lan) && !to.contains(lan)
//                        && !to.contains("自动") && !from.contains("自动")) {
//                    ToastUtils.show("源语言或者目标语言其中之一必须为" + lan);
//                    languageSelect.setText(language);
//                    return;
//                }
            }
        });
        exitDialog.show();
    }
    private void selectToLanguage(final TextView languageSelect) {
        final String str[] = LanguageUtils.toLangs;
        List<String> items = new ArrayList<String>();
        for (String s : str) {
            items.add(s);
        }

        SwListDialog exitDialog = new SwListDialog(SpeachTranslateDemoActivity.this,
                items);
        exitDialog.setItemListener(new SwListDialog.ItemListener() {

            @Override
            public void click(int position, String title) {

                //String language = languageSelect.getText().toString();
                languageSelect.setText(title);
                //String from = languageSelectFrom.getText().toString();
                //String to = languageSelectTo.getText().toString();

                //String lan = "中文";
//                if (!from.contains(lan) && !to.contains(lan)
//                        && !to.contains("自动") && !from.contains("自动")) {
//                    ToastUtils.show("源语言或者目标语言其中之一必须为" + lan);
//                    languageSelect.setText(language);
//                    return;
//                }
            }
        });
        exitDialog.show();
    }

    public void select(View view) {
        Intent intent = new Intent();
        intent.setType("audio/*"); //选择音频
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    public void record(View view) {
        try {
            audioFile = File.createTempFile("record_", ".wav");
            AuditRecorderConfiguration configuration = new AuditRecorderConfiguration.Builder()
                    .recorderListener(listener)
                    .handler(handler)
                    .rate(Constants.RATE_16000)
                    .uncompressed(true)
                    .builder();

            recorder = new ExtAudioRecorder(configuration);
            recorder.setOutputFile(audioFile.getAbsolutePath());
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordstop(View view) {
        try {
            int time = recorder.stop();
            if (time > 0) {
                if (audioFile != null) {
                    filePathText.setText(audioFile.getAbsolutePath());
                }
            }
            recorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void todetail(View view) {
        if (tr == null) {
            ToastUtils.show(this, "请先识别");
            return;
        }
        TranslateData td = new TranslateData(
                System.currentTimeMillis(), tr);
        TranslateDetailActivity.open(this, td, tr);

    }

    public void recognize(View view) {
        final String text = (String) filePathText.getText();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(SpeachTranslateDemoActivity.this, "请录音或选择音频文件", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        try {
            resultText.setText("正在识别，请稍等....");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startRecognize(text);
                }
            }).start();
        } catch (Exception e) {
        }
    }

    private void startRecognize(String filePath) {
        byte[] datas = null;
        try {
            datas = FileUtils.getContent(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String bases64 = EncryptHelper.getBase64(datas);

        String from = languageSelectFrom.getText().toString();
        String to = languageSelectTo.getText().toString();

        Language langFrom = LanguageUtils.getLangByName(from);

        Language langTo = LanguageUtils.getLangByName(to);

        //输入和输出音频格式都为wav格式
        tps = new SpeechTranslateParameters.Builder().source("youdaovoicetranslate")
                .from(langFrom).to(langTo)
                .rate(Constants.RATE_16000)//输入音频码率，支持8000,16000
                .voice(Constants.VOICE_NEW)//输出声音，支持美式女生、美式男生、英式女生、英式男生
                .timeout(100000)//超时时间
                .build();

        SpeechTranslate.getInstance(tps).lookup(bases64,"requestId",
                new TranslateListener() {
                    @Override
                    public void onResult(final Translate result, String input, String requestId) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                resultText.setText("翻译完成:" + result.getQuery());
                                tr = result;
                                toDetail.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onError(final TranslateErrorCode error, String requestId) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tr = null;
                                resultText.setText("翻译失败" + error.toString());
                                toDetail.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onResult(List<Translate> results, List<String> inputs, List<TranslateErrorCode> errors, String requestId) {

                    }
                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                filePathText.setText(FileUtils.getPath(this, uri));
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
