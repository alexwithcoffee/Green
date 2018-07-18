package com.example.hyunwoo.greeneco;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static java.lang.Double.parseDouble;

public class MainActivity extends AppCompatActivity {
    //대화상자
    private Activity activity;
    private View view;
    public static SendListAdapter sendListAdapter;
    public static ListView sendlistview;

    //OKHTTP
    private static final String TAG_RESULT = "result";
    private static final String TAG_LIST = "list1";
    private static final String TAG_LISTNAME = "listname";
    private static final String TAG_CREATE = "create";
    private static final String TAG_PRO = "pro";
    private static final String TAG_KG = "kg";
    private static final String TAG_KG2 = "kg2";
    private static final String TAG_WORK = "work";
    private static final String TAG_OUTKG = "outkg";
    private static final String TAG_BCODE = "bcode";
    private static final String TAG_PRODUCE = "produce";

    double kg2, outkg;
    String myList, BCODE, listname, work, create, pro, SUMKG, listname2, pro2, PACKING, listname4, create2, bcode, pro4, produce, produce2, kg3;
    JSONArray listcom, codelist, sumkglist, edit, packinglist = null;
    ArrayList<HashMap<String, String>> mlist;
    private Spinner spinner;

    //DAY
    long Now = System.currentTimeMillis();
    Date day = new Date(Now);
    SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd");
    String formatDate = dat.format(day);
    Calendar cal = Calendar.getInstance();
    TextView dt;

    EditText barcode;
    ListView listView;
    Button send;

    String result1;
    private int index;
    int i, listcheck = 0;
    int sendcheck = 1;


    private HttpConnection httpConn = HttpConnection.getInstance();
    private BarcodeAdapter adapter = new BarcodeAdapter();

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        sendListAdapter = new SendListAdapter(activity);

        InsertUtil.setInsertUtil();
        send = (Button) findViewById(R.id.send);

        //업체리스트
        mlist = new ArrayList<HashMap<String, String>>();
        getData("http://221.144.213.95/list.php");
        spinner = (Spinner) findViewById(R.id.spinner);

        //DAY
        dt = findViewById(R.id.dt);
        dt.setText(formatDate);
        cal.setTime(day);

        barcode = (EditText) findViewById(R.id.barcode);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        spinner.setAdapter((SpinnerAdapter) listcom);
        barcode.setInputType(0);

        //\n 엔터키 이벤트
        barcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int check = -1;
                int Count = 0;

                if (s.toString().contains("/") && s.toString().contains("\n")) {
                    for (BarcodeItem item : adapter.items) {
                        Count++;
                        if (barcode.getText().toString().contains("/") && item.getBc().equals(barcode.getText().toString().substring(0, barcode.getText().toString().indexOf("/")))) {
                            item.setKg((item.getKg()) + parseDouble(barcode.getText().toString().substring(barcode.getText().toString().indexOf("/") + 1)));
                            for (int j = 0; j < adapter.getCount(); j++)
                                if (item.getPro().equals(adapter.items.get(j).getPro()) && adapter.items.get(j).getNo().length() > 0) {
                                    adapter.items.get(j).setKg(adapter.items.get(j).getKg() + parseDouble(barcode.getText().toString().substring(barcode.getText().toString().indexOf("/") + 1)));
                                    break;
                                }
                            sendcheck = 0;
                            check = Count - 1;
                            adapter.items.set(check, item);
                            barcode.setText("");
                            listView.setAdapter(adapter);
                        }
                    }
                    if (check < 0 && barcode.getText().toString().contains("/")) {
                        sendData(barcode.getText().toString());
                    } else {
                        barcode.setText("");
                    }
                } else if (s.toString().contains("\n")) {
                    barcode.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //엔터키이벤트
        barcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int check = -1;
                int count = 0;
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        for (BarcodeItem item : adapter.items) {
                            count++;
                            if (barcode.getText().toString().contains("/") && item.getBc().equals(barcode.getText().toString().substring(0, barcode.getText().toString().indexOf("/")))) {
                                item.setKg((item.getKg()) + parseDouble(barcode.getText().toString().substring(barcode.getText().toString().indexOf("/") + 1)));
                                for (int j = 0; j < adapter.getCount(); j++)
                                    if (item.getPro().equals(adapter.items.get(j).getPro()) && adapter.items.get(j).getNo().length() > 0) {
                                        adapter.items.get(j).setKg(adapter.items.get(j).getKg() + parseDouble(barcode.getText().toString().substring(barcode.getText().toString().indexOf("/") + 1)));
                                        break;
                                    }
                                sendcheck = 0;
                                check = count - 1;
                                adapter.items.set(check, item);
                                barcode.setText("");
                                listView.setAdapter(adapter);
                            }
                        }
                        if (check < 0 && barcode.getText().toString().contains("/")) {
                            sendData(barcode.getText().toString());
                        } else {
                            barcode.setText("");
                        }
                        break;
                    default:
                        for (BarcodeItem item : adapter.items) {
                            count++;
                            if (barcode.getText().toString().contains("/") && item.getBc().equals(barcode.getText().toString().substring(0, barcode.getText().toString().indexOf("/")))) {
                                item.setKg((item.getKg()) + parseDouble(barcode.getText().toString().substring(barcode.getText().toString().indexOf("/") + 1)));
                                for (int j = 0; j < adapter.getCount(); j++)
                                    if (item.getPro().equals(adapter.items.get(j).getPro()) && adapter.items.get(j).getNo().length() > 0) {
                                        adapter.items.get(j).setKg(adapter.items.get(j).getKg() + parseDouble(barcode.getText().toString().substring(barcode.getText().toString().indexOf("/") + 1)));
                                        break;
                                    }
                                sendcheck = 0;
                                check = count - 1;
                                adapter.items.set(check, item);
                                barcode.setText("");
                                listView.setAdapter(adapter);
                            }
                        }
                        if (check < 0 && barcode.getText().toString().contains("/")) {
                            sendData(barcode.getText().toString());
                        } else {
                            barcode.setText("");
                        }
                }
                return false;
            }
        });

        //날짜 클릭
        dt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 0;
                adapter.items.clear();
                adapter.notifyDataSetChanged();
                sendData2(spinner.getSelectedItem().toString().substring(7, spinner.getSelectedItem().toString().indexOf("}")), String.valueOf(dt.getText()));
            }
        });

        //리스트뷰 전송
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = activity.getLayoutInflater().inflate(R.layout.sendlistview, null);
                sendListAdapter.list.clear();
                for (int l = 0; l < adapter.getCount(); l++) {
                    if (adapter.items.get(l).getNo().length() > 0 && adapter.items.get(l).getKg() > 0) {
                        sendListAdapter.addItem(adapter.items.get(l).getKind(), adapter.items.get(l).getCreate(), adapter.items.get(l).getKG(), adapter.items.get(l).getKg(), true);
                    } else if (adapter.items.get(l).getStatus() == 3) {
                        sendListAdapter.addItem(adapter.items.get(l).getKind(), adapter.items.get(l).getCreate(), adapter.items.get(l).getKG(), adapter.items.get(l).getKg(), false);
                    }
                }
                sendlistview = (ListView) view.findViewById(R.id.sendlistview);
                sendlistview.setAdapter(sendListAdapter);
                AlertDialog.Builder listViewDialog = new AlertDialog.Builder(activity);
                listViewDialog.setView(view);
                listViewDialog.setTitle("출하 품목");
                listViewDialog.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            InsertRequest request = new InsertRequest("http://221.144.213.95/listSend.php");
                            for (int j = 0; j < adapter.getCount(); j++) {
                                if (adapter.items.get(j).getBc().length() > 0 && adapter.items.get(j).getNo().length() == 0) {
                                    result1 = request.PHPRequest(adapter.items.get(j).getBc(), String.valueOf(dt.getText()), adapter.items.get(j).getKg(), Integer.parseInt(adapter.items.get(j).getPro()),
                                            String.valueOf(spinner.getSelectedItem().toString().substring(spinner.getSelectedItem().toString().indexOf("=") + 1, spinner.getSelectedItem().toString().indexOf("}"))));

                                    sendData5(adapter.items.get(j).getBc(), String.valueOf(dt.getText()), String.valueOf(spinner.getSelectedItem().toString().substring(spinner.getSelectedItem().toString().indexOf("=") + 1,
                                            spinner.getSelectedItem().toString().indexOf("}"))), -adapter.items.get(j).getKg(), 0, 0, Integer.parseInt(adapter.items.get(j).getPro()));
                                }
                            }
                            sendcheck = 1;
                            Toast.makeText(getApplication(), "전송 완료", Toast.LENGTH_SHORT).show();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplication(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                listViewDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "취소", Toast.LENGTH_SHORT).show();
                    }
                });
                listViewDialog.show();
            }
        });
        //스피너 클릭 이벤트
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i = 0;
                adapter.items.clear();
                adapter.notifyDataSetChanged();
                sendData2(spinner.getSelectedItem().toString().substring(7, spinner.getSelectedItem().toString().indexOf("}")), String.valueOf(dt.getText()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //바코드 입력 데이터
    private void sendData(final String bc) {
        // 네트워크 통신하는 작업은 무조건 작업스레드를 생성해서 호출 해줄 것!!
        new Thread() {
            public void run() {
                // 파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                httpConn.requestWebServer(bc, callback);
            }
        }.start();
    }

    private final Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("log", "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            BCODE = response.body().string();
            try {
                JSONObject jsonObj = new JSONObject(BCODE);
                codelist = jsonObj.getJSONArray(TAG_RESULT);
                int count = 0;
                sendcheck = 0;

                for (int j = 0; j < codelist.length(); j++) {
                    JSONObject object = codelist.getJSONObject(j);
                    listname = object.getString(TAG_LISTNAME);
                    create = object.getString(TAG_CREATE);
                    produce = object.getString(TAG_PRODUCE);
                    pro = object.getString(TAG_PRO);

                    for (BarcodeItem items : adapter.items) {
                        count++;
                        if (items.getPro().equals(pro) && adapter.items.get(count - 1).getNo().length() > 0) {
                            adapter.addItem(new BarcodeItem("", "", "", (double) 0, (double) 0, "", "", 1));
                            int q = adapter.getCount();
                            for (int m = count; adapter.getCount() > m; m++) {
                                adapter.items.set(q - 1, new BarcodeItem(adapter.items.get(q - 2).getNo(), adapter.items.get(q - 2).getKind(),
                                        adapter.items.get(q - 2).getCreate(), adapter.items.get(q - 2).getKG(), adapter.items.get(q - 2).getKg(),
                                        adapter.items.get(q - 2).getBc(), adapter.items.get(q - 2).getPro(), adapter.items.get(q - 2).status));
                                q--;
                            }
                            if (create.equals("null")) {
                                adapter.items.set(count, new BarcodeItem("", listname, produce, (double) 0, parseDouble(barcode.getText().toString().substring(barcode.getText().toString().indexOf("/") + 1)),
                                        barcode.getText().toString().substring(0, barcode.getText().toString().indexOf("/")), pro, 1));
                                items.setKg((items.getKg()) + parseDouble(barcode.getText().toString().substring(barcode.getText().toString().indexOf("/") + 1)));
                                break;
                            } else if (produce.equals("null")) {
                                adapter.items.set(count, new BarcodeItem("", listname, create, (double) 0, parseDouble(barcode.getText().toString().substring(barcode.getText().toString().indexOf("/") + 1)),
                                        barcode.getText().toString().substring(0, barcode.getText().toString().indexOf("/")), pro, 1));
                                items.setKg((items.getKg()) + parseDouble(barcode.getText().toString().substring(barcode.getText().toString().indexOf("/") + 1)));
                                break;
                            }
                        }
                    }
                    if (count == adapter.getCount()) {
                        if (create.equals("null")) {
                            adapter.addItem(new BarcodeItem("", listname, produce, (double) 0, parseDouble(barcode.getText().toString().substring(barcode.getText().toString().indexOf("/") + 1)),
                                    barcode.getText().toString().substring(0, barcode.getText().toString().indexOf("/")), pro, 3));
                            listcheck = 1;
                        } else if (produce.equals("null")) {
                            adapter.addItem(new BarcodeItem("", listname, create, (double) 0, parseDouble(barcode.getText().toString().substring(barcode.getText().toString().indexOf("/") + 1)),
                                    barcode.getText().toString().substring(0, barcode.getText().toString().indexOf("/")), pro, 3));
                            listcheck = 1;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    barcode.setText("");
                }
            });
        }
    };

    //발주량
    private void sendData2(final String company, final String date) {
        new Thread() {
            public void run() {
                httpConn.requestWebServer2(company, date, callback2);
            }
        }.start();
    }

    private final Callback callback2 = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("log", "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            SUMKG = response.body().string();
            Log.d("t", "" + SUMKG);
            try {
                JSONObject jsonObj = new JSONObject(SUMKG);
                sumkglist = jsonObj.getJSONArray(TAG_RESULT);

                for (int j = 0; j < sumkglist.length(); j++) {
                    JSONObject object2 = sumkglist.getJSONObject(j);

                    listname2 = object2.getString(TAG_LISTNAME);
                    kg2 = Double.valueOf(object2.getString(TAG_KG));
                    kg3 = object2.getString(TAG_KG2);
                    work = object2.getString(TAG_WORK);
                    pro2 = object2.getString(TAG_PRO);

                    if (kg3 == "null" && kg2 > 0) {
                        i++;
                        adapter.addItem(new BarcodeItem(i + "", listname2, work, kg2, (double) 0, "", pro2, 1));
                    } else if (kg3 != "null" && Double.parseDouble(kg3) > 0) {
                        i++;
                        adapter.addItem(new BarcodeItem(i + "", listname2, work, Double.parseDouble(kg3), (double) 0, "", pro2, 1));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    //sendData3(spinner.getSelectedItem().toString().substring(7, spinner.getSelectedItem().toString().indexOf("}")), String.valueOf(dt.getText()));
                    sendData4(spinner.getSelectedItem().toString().substring(7, spinner.getSelectedItem().toString().indexOf("}")), String.valueOf(dt.getText()));
                }
            });
        }
    };

    //PackingOut 리스트
    private void sendData4(final String compp3, final String dday3) {
        new Thread() {
            public void run() {
                httpConn.requestWebServer4(compp3, dday3, callback4);
            }
        }.start();
    }

    private final Callback callback4 = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("log", "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            PACKING = response.body().string();
            try {
                JSONObject jsonObj = new JSONObject(PACKING);
                packinglist = jsonObj.getJSONArray(TAG_RESULT);
                int count = 0;
                int check;

                for (int j = 0; j < packinglist.length(); j++) {
                    JSONObject object3 = packinglist.getJSONObject(j);

                    listname4 = object3.getString(TAG_LISTNAME);
                    create2 = object3.getString(TAG_CREATE);
                    outkg = Double.valueOf(object3.getString(TAG_OUTKG));
                    bcode = object3.getString(TAG_BCODE);
                    pro4 = object3.getString(TAG_PRO);
                    produce2 = object3.getString(TAG_PRODUCE);
                    check = 0;

                    for (BarcodeItem items : adapter.items) {
                        count++;
                        if (items.getPro().equals(pro4) && items.getNo().length() > 0) {
                            adapter.addItem(new BarcodeItem("", "", "", (double) 0, (double) 0, "", "", 1));
                            int q = adapter.getCount();
                            for (int m = count; adapter.getCount() > m; m++) {
                                adapter.items.set(q - 1, new BarcodeItem(adapter.items.get(q - 2).getNo(), adapter.items.get(q - 2).getKind(),
                                        adapter.items.get(q - 2).getCreate(), adapter.items.get(q - 2).getKG(), adapter.items.get(q - 2).getKg(), adapter.items.get(q - 2).getBc(),
                                        adapter.items.get(q - 2).getPro(), adapter.items.get(q - 2).status));
                                q--;
                            }
                            if (create2.equals("null")) {
                                adapter.items.set(count, new BarcodeItem("", listname4, produce2, (double) 0, outkg, bcode, pro4, 1));
                                items.setKg((items.getKg()) + outkg);
                                count = 0;
                                check = 1;
                                break;
                            } else if (produce2.equals("null")) {
                                adapter.items.set(count, new BarcodeItem("", listname4, create2, (double) 0, outkg, bcode, pro4, 1));
                                items.setKg((items.getKg()) + outkg);
                                count = 0;
                                check = 1;
                                break;
                            }
                        }
                    }
                    if (check == 0) {
                        if (create2.equals("null")) {
                            adapter.addItem(new BarcodeItem("", listname4, produce2, (double) 0, outkg, bcode, pro4, 3));
                            listcheck = 1;
                        }
                        if (produce2.equals("null")) {
                            adapter.addItem(new BarcodeItem("", listname4, create2, (double) 0, outkg, bcode, pro4, 3));
                            listcheck = 1;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    //발주 목록 삭제(DB 무게0)
    private void sendData5(final String barcd, final String date, final String company, final double amount, final double editkg, final int check, final int prot) {
        new Thread() {
            public void run() {
                httpConn.requestWebServer5(barcd, date, company, amount, editkg, check, prot, callback5);
            }
        }.start();
    }

    private final Callback callback5 = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
        }
    };

    //업체리스트
    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myList);
            listcom = jsonObj.getJSONArray(TAG_RESULT);

            for (int i = 0; i < listcom.length(); i++) {
                JSONObject c = listcom.getJSONObject(i);
                String id = c.getString(TAG_LIST);

                HashMap<String, String> persons = new HashMap<String, String>();
                persons.put(TAG_LIST, id);
                mlist.add(persons);
            }
            SpinnerAdapter adapter = new SimpleAdapter(this, mlist, R.layout.spinner_layout, new String[]{TAG_LIST}, new int[]{R.id.txt});

            spinner.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //업체리스트 데이터
    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myList = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    //바코드 Adapter
    class BarcodeAdapter extends BaseAdapter {

        ArrayList<BarcodeItem> items = new ArrayList<>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(BarcodeItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BarcodeItemView view = new BarcodeItemView(getApplicationContext());

            BarcodeItem item = items.get(position);

            view.setNo(item.getNo());
            view.setKind(item.getKind());
            view.setDday(item.getCreate());
            view.setName(item.getKG());
            view.setKg(item.getKg());
            view.setBc(item.getBc());
            view.setPro(item.getPro());

            if (adapter.items.get(position).getKG().equals(adapter.items.get(position).getKg()) && adapter.items.get(position).getNo().length() > 0) {
                view.setBackgroundColor(0xFF99FF99);
            }
            if (adapter.items.get(position).status == 3) {
                view.setBackgroundColor(0xFFFF9999);
            }
            if (adapter.items.get(position).getKG() < adapter.items.get(position).getKg() && adapter.items.get(position).getNo().length() > 0) {
                view.setBackgroundColor(0xFF87CEEB);
            }

            if (listcheck == 1 && dt.getText().toString().equals(formatDate)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("출하 품목을 확인해주세요.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                listcheck = 0;
            }
            return view;
        }
    }

    //날짜 증가
    public void mOnClickPLUS(View v) {
        cal.add(Calendar.DATE, 1);
        dt.setText(dat.format(cal.getTime()));
        i = 0;
        adapter.items.clear();
        adapter.notifyDataSetChanged();
    }

    //날짜 감소
    public void mOnClickMINUS(View v) {
        cal.add(Calendar.DATE, -1);
        dt.setText(dat.format(cal.getTime()));
        i = 0;
        adapter.items.clear();
        adapter.notifyDataSetChanged();
    }

    //리스트뷰 롱클릭(수정/삭제)
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == listView) {
            menu.add(0, 1, 0, "수정");//두번째 인자는 ItemId임
            menu.add(0, 2, 0, "삭제");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        index = menuInfo.position;
        switch (item.getItemId()) {
            case 1:
                final AlertDialog.Builder edit2 = new AlertDialog.Builder(MainActivity.this);
                edit2.setTitle("무게 변경");
                edit2.setMessage("품목 = " + adapter.items.get(index).getKind() + '\n' + "현재 무게 = " + adapter.items.get(index).getKg());
                final EditText edit3 = new EditText(MainActivity.this);
                edit2.setView(edit3);
                edit3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                edit2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        double value = parseDouble(edit3.getText().toString());
                        double value3 = value - adapter.items.get(index).getKg();
                        dialog.dismiss();

                        if (adapter.items.get(index).status == 1 && adapter.items.get(index).getNo().length() == 0) {
                            for (int j = 0; j < adapter.getCount(); j++) {
                                if (adapter.items.get(index).getPro().equals(adapter.items.get(j).getPro())) {
                                    adapter.items.get(j).setKg(adapter.items.get(j).getKg() + value3);
                                    adapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                            sendData5(adapter.items.get(index).getBc(), String.valueOf(dt.getText()), String.valueOf(spinner.getSelectedItem().toString().substring(spinner.getSelectedItem().toString().indexOf("=") + 1,
                                    spinner.getSelectedItem().toString().indexOf("}"))), value, value, 1, Integer.parseInt(adapter.items.get(index).getPro()));
                            adapter.items.get(index).setKg(value);
                        } else if (adapter.items.get(index).status != 1 && adapter.items.get(index).getNo().length() == 0) {
                            sendData5(adapter.items.get(index).getBc(), String.valueOf(dt.getText()), String.valueOf(spinner.getSelectedItem().toString().substring(spinner.getSelectedItem().toString().indexOf("=") + 1,
                                    spinner.getSelectedItem().toString().indexOf("}"))), value, value, 1, Integer.parseInt(adapter.items.get(index).getPro()));
                            adapter.items.get(index).setKg(value);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                edit2.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                edit2.show();
                break;
            case 2:
                if (adapter.items.get(index).getNo().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("알림")
                            .setMessage("삭제하시겠습니까?")
                            .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    if (adapter.items.get(index).getNo().length() == 0) {
                                        sendData5(adapter.items.get(index).getBc(), String.valueOf(dt.getText()), String.valueOf(spinner.getSelectedItem().toString().substring(spinner.getSelectedItem().toString().indexOf("=") + 1,
                                                spinner.getSelectedItem().toString().indexOf("}"))), adapter.items.get(index).getKg(), 0, 2, Integer.parseInt(adapter.items.get(index).getPro()));
                                        for (int j = 0; j < adapter.getCount(); j++) {
                                            if (adapter.items.get(index).getPro().equals(adapter.items.get(j).getPro()) && adapter.items.get(j).getNo().length() > 0) {
                                                adapter.items.get(j).setKg(adapter.items.get(j).getKg() - adapter.items.get(index).getKg());
                                                break;
                                            }
                                        }
                                        adapter.items.remove(index);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
        }
        return true;
    }
}