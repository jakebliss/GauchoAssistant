package com.jakebliss.gauchoassistant.gauchoassistant;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import static java.lang.System.exit;

public class GoldScraper extends AsyncTask<String, Integer, Long> {
    public static final String SPLIT_INTERNET_URL = "https://my.sa.ucsb.edu/gold/StudentSchedule.aspx";
    public static final String SPLIT_LOGIN = "https://my.sa.ucsb.edu/gold/Login.aspx";
    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36";
    Context myContext;
    ClassSchedule mySched = new ClassSchedule();
    String test;

    String username = "";
    String password = "";
    ArrayList<String> courseTitles2 = new ArrayList<String>();
    ArrayList<String> dayList2 = new ArrayList<String>();
    ArrayList<String> timeList2 = new ArrayList<String>();
    ArrayList<String> locList2 = new ArrayList<String>();

    public  GoldScraper(Context ctx, String user, String pass) {
        this.myContext = ctx;
        this.username = user;
         this .password = pass;
    }

    @Override
    protected Long doInBackground(String... strings) {
        Connection.Response res = null;
        Connection.Response response = null;
        Document init = null;
        Elements elems = null;
        Elements elems2 = null;
        Elements elems3 = null;

        try {
            response = Jsoup.connect(SPLIT_LOGIN).userAgent(USER_AGENT)
                    .method(Connection.Method.GET)
                    .execute();
            init = response.parse();

             elems = init.select("input#__VIEWSTATE");
             elems2 = init.select("input#__EVENTVALIDATION");
             elems3 = init.select("input#__VIEWSTATEGENERATOR");

            Log.d("ELEMS ARE THE FOLOWING", "adfasdfasdf");
            Log.d("__VIEWSTATE", elems.attr("value"));
            Log.d("__EVENTVALIDATION",  elems2.attr("value"));
            Log.d("__VIEWSTATEGENERATOR", elems3.attr("value"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //VIEW STATE: /wEPDwUKMTM5OTE1ODA1Ng9kFgJmD2QWAgIDD2QWCmYPDxYCHgdWaXNpYmxlaGQWAgIBD2QWAgIBDw8WAh8AaGRkAgEPFgIfAGhkAgIPFgIfAGhkAgMPZBYMAgIPDxYCHwBoZGQCAw8PZBYCHgxhdXRvY29tcGxldGUFA29mZmQCBA8PZBYCHwEFA29mZmQCBg9kFgJmD2QWBAIBDw8WAh8AaGRkAgcPFgIfAGhkAgcPDxYCHgtOYXZpZ2F0ZVVybAUtLy9teS5zYS51Y3NiLmVkdS9QZXJtUGluUmVzZXQvRm9yZ290UGVybS5hc3B4ZGQCCA8PFgIfAgUvLy9teS5zYS51Y3NiLmVkdS9QZXJtUGluUmVzZXQvUGVybVBpblJlc2V0LmFzcHhkZAIEDw8WAh8AaGQWAgIBD2QWAgIBDw8WAh8AaGRkZFWCuJoPk7ARvPD4xG4fSJedZVYd
        //VIEW STATE GENER: 00732C32
        //EVENT VAL: /wEdAAdbKm4OU/lsarSPEWzw3woTFPojxflIGl2QR/+/4M+LrK6wLDfR+5jffPpLqn7oL3ttZruIm/YRHYjEOQyILgzL2Nu6XIik3f0iXq7Wqnb39/ZNiE/A9ySfq7gBhQx160NmmrEFpfb3YUvL+k7EbVnKgIKH2XlDUw30P837MyfVDMpYxIk=

        Log.d("adfasdfasdf", "*********************************************************************************************************************************************************************");
        //TODO: Change the XXXXXXX to an attribute of a textfield.
        try {
            res = Jsoup.connect(SPLIT_LOGIN)
                    .data(
                            "__VIEWSTATE", elems.attr("value"),
                            "__VIEWSTATEGENERATOR",elems3.attr("value"),
                            "__EVENTVALIDATION",  elems2.attr("value"),
                            "ctl00$pageContent$userNameText", username,
                            "ctl00$pageContent$passwordText",password,
                            "ctl00$pageContent$loginButton", "Login")
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .userAgent(USER_AGENT)
                    .execute();


        Document doc = Jsoup.connect(SPLIT_INTERNET_URL)
                .cookies(res.cookies())
                .userAgent(USER_AGENT)
                .get();

        System.out.println(doc);


        //<span id="ctl00_pageContent_CourseList_ctl00_CourseHeadingLabel">CMPSC   162   - PROGRAM LANGUAGES   </span>
        //Elements courseTitle = doc.select("span#ctl00_pageContent_CourseList_ctl00_CourseHeadingLabel");
        ArrayList<String> myCourses = getCourses(doc);
        System.out.println("COURSE TITLE IS: " + getCourses(doc).toString());
        //[CMPSC 162 - PROGRAM LANGUAGES 55418 Grading: L 4.0 Units Days T R Time3:30 PM-4:45 PM LocationPhelps Hall, 3526 InstructorHARDEKOPF B C Days T Time5:00 PM-6:50 PM LocationPhelps Hall, 3526 InstructorEMRE M Course Info Drop Modify Switch, CMPSC 184 - MOBILE APP DEV 08995 Grading: L 4.0 Units DaysM W Time2:00 PM-3:15 PM LocationArts Building, 1349 InstructorHOLLERER T Days F Time11:00 AM-11:50 AM LocationPhelps Hall, 3526 InstructorSAYYAD E Course Info Drop Modify Switch, CMPSC 189A - SR CMPTR PRJCT 09019 Grading: L 4.0 Units DaysM Time12:00 PM-1:50 PM LocationPhelps Hall, 1437 InstructorKRINTZ C Days R Time5:00 PM-5:50 PM LocationESB, 1003 InstructorBAKO S C Course Info Drop Modify Switch, WRIT 107T - TECHNICAL WRITING 51011 Grading: L 4.0 Units Days T R Time9:30 AM-10:45 AM LocationHSSB, 1233 InstructorFRANK D M Course Info Drop Modify Switch, Total Units: 16.0]

        System.out.println("DOC IS______");
        String s = readSettings(myContext);
        System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Connection.Response res2 = null;



        //<span id="ctl00_pageContent_CourseList_ctl00_CourseHeadingLabel">CMPSC   162   - PROGRAM LANGUAGES   </span>
        //<div id="ctl00_pageContent_ScheduleGrid" class="datatableNew" style="margin-top: 5px;">
        //<div class="scheduleItem">
        //<div class="row">
        //<div class="col-sm-12 col-xs-12">
        //                                <p id="ctl00_pageContent_CourseList_ctl00_courseCell" class="courseTitle">
        //                                    <span id="ctl00_pageContent_CourseList_ctl00_CourseHeadingLabel">CMPSC   162   - PROGRAM LANGUAGES   </span>
        //                                </p>
        //                            </div>

        return null;
    }


    public ArrayList<String> getCourses(Document doc) {
        ArrayList<String> courseTitles = new ArrayList<String>();
        ArrayList<String> dayList = new ArrayList<String>();
        ArrayList<String> timeList = new ArrayList<String>();
        ArrayList<String> locList = new ArrayList<String>();


        ClassSchedule myCS = new ClassSchedule();

        MyClass mc = new MyClass();
        //<div class="col-sm-12 col-xs-12">
        Elements sched = doc.select("div.col-sm-12");
        for (Element elem : sched) {
            courseTitles.add(elem.text());

        }
        courseTitles.remove(courseTitles.size()-1);
        courseTitles.remove(courseTitles.size()-1);

        //<div class="col-lg-days col-lg-push-instructor col-md-push-instructor col-lg-push-0 col-sm-days col-sm-push-3 col-xs-2">
        sched = doc.select("div.col-lg-days");
        for (Element elem : sched) {
            dayList.add(elem.text());
        }

        //<div class="col-lg-time col-lg-push-instructor col-md-push-instructor col-md-time col-sm-4 col-sm-push-3  col-xs-5">
        sched = doc.select("div.col-lg-time");
        for (Element elem : sched) {
            timeList.add(elem.text());
        }

        sched = doc.select("div.col-lg-location");
        for (Element elem : sched) {
            locList.add(elem.text());
        }

        this.courseTitles2 = courseTitles;
        this.timeList2 = timeList;
        this.dayList2 = dayList;
        this.locList2 = locList;

        String single = "";
        for (int i = 0; i < courseTitles.size(); i++){
            single += "Title"+ Integer.toString(i) + courseTitles.get(i) + '$';
            single += dayList.get(i) + '$';
            single += timeList.get(i) + '$';
            single += locList.get(i) + '$';
            single += "****" + Integer.toString(i) +"";
        }

        writeSettings(single, myContext);


        //System.out.println("The zero'th is: " + courseTitles.get(0));
//        Elements sched = doc.select("div#ctl00_pageContent_ScheduleGrid");
//        for (Element elem : sched.select("div.scheduleItem")) {
//            courseTitles.add(elem.text());
//        }
        return courseTitles;
    }

    private void writeSettings(String data,Context context) {

        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(myContext.openFileOutput("my_schedule.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }



    private String readSettings(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("my_schedule.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);

                }

                inputStream.close();
                ret = stringBuilder.toString();  //.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }



}
