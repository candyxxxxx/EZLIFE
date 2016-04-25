package com.ee5415.group2.ezlife.functions.map;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.DrivingRouteOverlay;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.ee5415.group2.ezlife.R;
import com.ee5415.group2.ezlife.functions.map.Util.AMapUtil;
import com.ee5415.group2.ezlife.functions.map.Util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class Map extends FragmentActivity implements
        AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, TextWatcher,
        PoiSearch.OnPoiSearchListener, View.OnClickListener, Inputtips.InputtipsListener, LocationSource,
        AMapLocationListener, AMapNaviListener, RouteSearch.OnRouteSearchListener {
    private AMap aMap;
    private AutoCompleteTextView searchText;// 输入搜索关键字
    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private UiSettings mUiSettings;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private MyLocationStyle myLocationStyle = new MyLocationStyle();
    private LatLonPoint endNaviLatPot;
    private LatLonPoint startNaviLatPot;
    // 驾车线路：路线规划、模拟导航、实时导航按钮
//	private Button mDriveRouteButton;
    // 地图和导航资源
//	private MapView mMapView;
    private AMapNavi aMapNavi;
    //	private RouteOverLay mRouteOverLay;
    private RouteSearch.DriveRouteQuery q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aMapNavi = AMapNavi.getInstance(this);
//		aMapNavi.addAMapNaviListener(this);
//		aMapNavi.setEmulatorNaviSpeed(150);
        setContentView(R.layout.activity_map);
        init(savedInstanceState);
    }

    /**
     * 初始化AMap对象
     */
    private void init(Bundle savedInstanceState) {
        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            mUiSettings = aMap.getUiSettings();
            setUpMap();
        }
        if (aMap != null) {
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
            myLocationStyle.strokeColor(R.color.gray_lite);// 设置圆形的边框颜色
            myLocationStyle.radiusFillColor(R.color.gray_lite);// 设置圆形的填充颜色
            myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
            aMap.setMyLocationStyle(myLocationStyle);
            mUiSettings
                    .setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
            mUiSettings.setScaleControlsEnabled(true);
            mUiSettings.setZoomControlsEnabled(true);
            aMap.setLocationSource(this);// 设置定位监听

            mUiSettings.setMyLocationButtonEnabled(true); // 是否显示默认的定位按钮
            aMap.setMyLocationEnabled(true);
            mUiSettings.setScrollGesturesEnabled(true);
            mUiSettings.setZoomGesturesEnabled(true);
        }
    }

    /**
     * 设置页面监听
     */
    private void setUpMap() {
        Button searButton = (Button) findViewById(R.id.searchButton);
        searButton.setOnClickListener(this);
        searchText = (AutoCompleteTextView) findViewById(R.id.keyWord);
        searchText.addTextChangedListener(this);// 添加文本输入框监听事件
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件

    }

    /**
     * 点击搜索按钮
     */
    public void searchButton() {
        keyWord = AMapUtil.checkEditText(searchText);
        if ("".equals(keyWord)) {
            ToastUtil.show(Map.this, "请输入搜索关键字");
            return;
        } else {
            doSearchQuery();
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
            myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        }
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        showProgressDialog();// 显示进度框
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        query.setCityLimit(true);

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        // 调起高德地图app
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng endLatLng = marker.getPosition();
                startNaviLatPot = new LatLonPoint(endLatLng.latitude,
                        endLatLng.longitude);
                endNaviLatPot = new LatLonPoint(mlocationClient.getLastKnownLocation().getLatitude(),
                        mlocationClient.getLastKnownLocation().getLongitude());
                RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startNaviLatPot, endNaviLatPot);
                System.out.println(startNaviLatPot + " " + endNaviLatPot);
                rote(startNaviLatPot, endNaviLatPot);
            }

        });
        return view;
    }

    protected void rote(LatLonPoint endNaviLatPot, LatLonPoint startNaviLatPot) {
        RouteSearch routeSerch = new RouteSearch(Map.this);
        routeSerch.setRouteSearchListener(Map.this);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startNaviLatPot, endNaviLatPot);

        q = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault,
                null, null, "");
        routeSerch.calculateDriveRouteAsyn(q);// 异步路径规划驾车模式查询 //

    }

    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(Map.this, infomation);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!AMapUtil.IsEmptyOrNullString(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, "");
            Inputtips inputTips = new Inputtips(Map.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }


    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == 1000) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtil.show(Map.this,
                                R.string.no_result);
                    }
                }
            } else {
                ToastUtil.show(Map.this,
                        R.string.no_result);
            }
        } else {
            ToastUtil.showerror(Map.this, rCode);
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {
        // TODO Auto-generated method stub

    }

    /**
     * Button点击事件回调方法
     */
    @Override
    public void onClick(View v) {
        searchButton();
    }




    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 1000) {// 正确返回
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            searchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showerror(Map.this, rCode);
        }
    }
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
    //-----------------------------------------------------------------------------------------------------
    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {
    }

    @Override
    public void onStartNavi(int i) {
    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteSuccess() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getPaths() != null
                    && result.getPaths().size() > 0) {
                DriveRouteResult driveRouteResult = result;
                DrivePath drivePath = driveRouteResult.getPaths().get(0);
                aMap.clear();// 清理地图上的所有覆盖物
                aMap.setMyLocationStyle(myLocationStyle);
                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                        this,aMap, drivePath, driveRouteResult.getStartPos(),
                        driveRouteResult.getTargetPos());
                drivingRouteOverlay.removeFromMap();
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();

            } else {
                Toast.makeText(Map.this, "Sorry, no such data found", Toast.LENGTH_SHORT).show();
            }
        } else if (rCode == 27) {
            Toast.makeText(Map.this, "Error, please make sure conneting to the Internet", Toast.LENGTH_SHORT).show();
        } else if (rCode == 32) {
            Toast.makeText(Map.this, "验证无效", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error code" + rCode, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }
}
