package com.example.baidumaptest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.example.baidumaptest.MyOrientationListener.OnOrientationListener;


public class MainActivity extends Activity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener, OnGetGeoCoderResultListener, OnGetRoutePlanResultListener {
	//地图
	private MapView mMapView = null;  
	private BaiduMap mBaiduMap;
	private Button rotate;//旋转
	private Button satellite;//设置w卫星图
//	private PoiSearch poiSearch;
//	MKMapViewListener mMapListener = null;        //处理地图事件回调  
	
	private Button traffic;
	private Button normal;
	private Button hot;
	
	//搜索  
    private EditText mkey;    
    private EditText mcity;  
    private String city;
    private String City = "北京市";
    private Button query;  
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;  
    private AutoCompleteTextView keyWorldsView = null;  
    private ArrayAdapter<String> sugAdapter = null;  
    private int load_Index = 0;  

    private EditText start,end;//起点终点
    private Button drive,transit,walk,shang,xia;
    private TextView tv2,qidian,zhongdian,listnodetitle;
    private Button chakan,btn_over; //查看详情,关闭详情
    private LinearLayout linear;
    private ListView listView;
    
    GeoCoder mSearch = null; // 地理编码搜索模块，也可去掉地图模块独立使用
    RoutePlanSearch Search = null; // 线路规划搜索模块
    int nodeIndex = -1;//节点索引,供浏览节点时使用
    RouteLine route = null;
    OverlayManager routeOverlay=null;
    boolean useDefaultIcon = false;
    private TextView popupText = null;//泡泡view
  //获取节结果信息
    LatLng nodeLocation = null; //节点位置
    String nodeTitle = null; //节点信息
    
    static View mPopView=null;//点击mark时弹出的气泡view
    private double cityLng; //当前城市 - 维度
    private double positionLng;//当前位置 - 经度
    
    public int SHOW1=0;
    public int SHOW2=0;
    public int SHOW3=0;
    
    private static StringBuilder sb;
//实时线路规划
//    /**
//     * 获取位置时间间隔单位
//     */
//    private final int time=1000*9;
//    /**
//     * 是否获取路线规划
//     */
//    private boolean isGetRoute=false;
//    /**
//     * 是否获取新路线
//     */
//    private boolean isGetNewRoute=true;
//    /**
//     * 定位位置数据
//     */
//    private List<LatLng> pointList=new ArrayList<LatLng>();
//    /**
//     * 描述即将发生变化
//     */
//    protected MapStatusUpdate msUpdate=null;
//    /**
//     * 路线覆盖物
//     */
//    private PolylineOptions polyline=null;
//    protected OverlayOptions overlay;
//    /**
//     * 手机加速度感应器服务注册
//     */
//    private SensorManager sm=null;
//    private Acc acc=new Acc();
//    /**
//     * 最大距离单位（米）
//     */
//    private final Double MaxDistance=90.00;
//    /**
//     * 最小距离单位（米）
//     */
//    private final Double MinDistance=2.0;
//    /**
//     * 电源锁
//     */
//    public static WakeLock wakeLock=null;
//    private PowerReceiver powerReceiver=new PowerReceiver();
//    /**
//     * 最近位置信息
//     */
//    private LatLng latLng;
//    /**
//     * 因距离太大丢失的点数
//     */
//    private int LostLoc=0;
//    /**
//     * 第一次定位丢失的点数
//     */
//    private int FirstLoc=0;
//    /**
//     * 路线规划监听
//     */
//    private RoutePlanSearch mSearch1;
//    /**
//     * 当前位置，终点位置
//     */
//    private LatLng ll,EndLL;
//    /**
//     * 路线规划等待进度框
//     */
//    private ProgressDialog progressDialog;
//    /**
//     * 获取位置距离常量
//     */
//    private int constant=0;    
	//设置经纬度
	private double mLatitude;
	private double mLongtitude;
    //是否显示覆盖物 1-显示 0-不显示  
//    private int isShowOverlay = 1;
	//定位SDK的核心类
	private LocationClient mLocationClient; 
	//是否首次定位
	private boolean isFirstLoc=true;
	//设置监听函数
	public MyLocationListerner mLocationListener;
     private Context context;
 // 自定义定位图标  
    private BitmapDescriptor mIconLocation;  
    private com.baidu.mapapi.map.MyLocationConfiguration.LocationMode mLocationMode;//控制模式
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);   
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现          
//        sm=(SensorManager) getSystemService(SENSOR_SERVICE);
        
        SDKInitializer.initialize(getApplicationContext());  
        requestWindowFeature(Window.FEATURE_NO_TITLE);       
        setContentView(R.layout.activity_main);  
        this.context=this;
        
        mPoiSearch=PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener( this);
        mSuggestionSearch=SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        mcity=(EditText) findViewById(R.id.city_edittext);
        keyWorldsView=(AutoCompleteTextView) findViewById(R.id.searchkey);
        query=(Button) findViewById(R.id.search);
        sugAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        keyWorldsView.setAdapter(sugAdapter);
        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        keyWorldsView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				if (cs.length()<=0) {
					return;
				}
				city=mcity.getText().toString();
				mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
						.keyword(cs.toString()).city(city));
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});

        
        IntentFilter filter=new IntentFilter();	      
        //获取地图控件引用  
        mMapView = (MapView) findViewById(R.id.map_view);  
        mBaiduMap=mMapView.getMap();
//        receiver=new MyBroadcastReceiver();  
        init();
        //开启定位图层
        mBaiduMap.setMyLocationEnabled(true);       
        //定位初始化
      //注意: 实例化定位服务 LocationClient类必须在主线程中声明 并注册定位监听接口  
        mLocationClient=new LocationClient(this);
        mLocationListener = new MyLocationListerner();
        mLocationClient.registerLocationListener(mLocationListener);//可能这里要改
        LocationClientOption option=new LocationClientOption();
        option.setOpenGps(true);//打开GPS
        option.setCoorType("bd09ll");//设置坐标类型
        option.setScanSpan(1000);//设置定位的时间间隔为1000ms
        option.setIsNeedAddress(true);//返回当前的位置信息，如果不设置为true，默认就为false，就无法获得位置的信息  
        
        mLocationClient.setLocOption(option);//设置定位参数
        //初始化图标
        mIconLocation=BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
        myOrientationListener=new MyOrientationListener(context);
        myOrientationListener.setOnOrientationListener(new OnOrientationListener() {
			
			@Override
			public void onOrientationChanged(float x) {
				mCurrentX=x;				
			}
		});
    
        mLocationClient.start();//调用此方法定位
        
        start=(EditText) findViewById(R.id.start);
        end=(EditText) findViewById(R.id.end);        
        linear=(LinearLayout) findViewById(R.id.linear);
        tv2 = (TextView) findViewById(R.id.tv2);
        chakan = (Button) findViewById(R.id.chakan);
        btn_over = (Button) findViewById(R.id.btn_over);
        qidian = (TextView) findViewById(R.id.qidian);
        zhongdian = (TextView) findViewById(R.id.zhongdian);
        listView = (ListView) findViewById(R.id.listview);
        
     // 初始化搜索模块，注册事件监听 /地理编码
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
     // 初始化搜索模块，注册事件监听 /线路规划
        Search = RoutePlanSearch.newInstance();
        Search.setOnGetRoutePlanResultListener(this);
//        hidden();隐藏不需要
        
        btn_over.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {							
			        linear.setVisibility(View.GONE);
			        chakan.setVisibility(View.GONE);            			 			
			}
		});        
        rotate=(Button) findViewById(R.id.rotate);//设置旋转
        rotate.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				//以屏幕中心点旋转
	            MapStatus mapStatus = mBaiduMap.getMapStatus(); //获取当前地图的状态
	            float rotate =  mapStatus.rotate;  //获取旋转角度
	            Log.i("旋转角度", "rotate"+rotate);
	            //用获取到的当前角度+30就是每次都旋转30°  范围0-360°
	            MapStatus rotates =new MapStatus.Builder().rotate(rotate+30).build();
	            //更新地图的选择
	            MapStatusUpdate rotateStatus = MapStatusUpdateFactory.newMapStatus(rotates);
	            mBaiduMap.setMapStatus(rotateStatus);				
			}
		});        
        satellite=(Button) findViewById(R.id.satellite);//设置卫星图
        satellite.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);	
				mBaiduMap.setBaiduHeatMapEnabled(false);	
				mBaiduMap.setTrafficEnabled(false);
			}
		});
        hot=(Button) findViewById(R.id.hot);
        hot.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {	
				if (mBaiduMap.isBaiduHeatMapEnabled()) {
					mBaiduMap.setBaiduHeatMapEnabled(false);	
				}else {
					mBaiduMap.setBaiduHeatMapEnabled(true);	
				}				
			}
		});
        traffic=(Button) findViewById(R.id.traffic);
        traffic.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				if (mBaiduMap.isTrafficEnabled()) {
					mBaiduMap.setTrafficEnabled(false);
				}else{
				mBaiduMap.setTrafficEnabled(true);
				}
			}
		});
        normal=(Button) findViewById(R.id.Normal);
        normal.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				 mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			}
		});               
       
        
//        //7.地图事件，设置地图单独监听
//        mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
//			
//			@Override
//			public boolean onMapPoiClick(MapPoi arg0) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public void onMapClick(LatLng point) {
//				 mBaiduMap.hideInfoWindow();
//				
//			}
//
//		});
//        //设置地图覆盖物监听
//        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
//			
//			@Override
//			public boolean onMarkerClick(Marker arg0) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		});
//      //设置地图双击监听
//        mBaiduMap.setOnMapDoubleClickListener(new OnMapDoubleClickListener() {
//
//                    @Override
//                    public void onMapDoubleClick(LatLng arg0) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
//        //发起截图请求
//        mBaiduMap.snapshot(new SnapshotReadyCallback() {
//
//                    @Override
//                    public void onSnapshotReady(Bitmap arg0) {
//                        // TODO Auto-generated method stub
//
//                    }
//                });
      //网络错误
//	      filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
//	      //校验key失败
//	      filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
//	      registerReceiver(receiver, filter);
//	      
    }      
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	//开启定位
    	mBaiduMap.setMyLocationEnabled(true);
    	if (!mLocationClient.isStarted()) {
			mLocationClient.start();
		}
    	//开启方向传感器
    	myOrientationListener.start();
   
    }
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	//停止定位
    	mBaiduMap.setMyLocationEnabled(false);
    	mLocationClient.stop();
    	//停止方向传感器
    	myOrientationListener.stop();
    }
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();  
        }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();  
        }  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
//        mLocationClient.stop();                       //退出时销毁定位  
//        mBaiduMap.setMyLocationEnabled(false);   //关闭定位图层
        mMapView.onDestroy();  
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        mSearch.destroy();
        //线路规划搜索模块
        Search.destroy();
//        unregisterReceiver(receiver);
    }  
    @Override  
    protected void onSaveInstanceState(Bundle outState) {  
        super.onSaveInstanceState(outState);  
    }  
  
    @Override  
    protected void onRestoreInstanceState(Bundle savedInstanceState) {  
        super.onRestoreInstanceState(savedInstanceState);  
    }  
    public void searchButtonProcess(View v) {
    	 //城市  
        EditText editCity = (EditText) findViewById(R.id.city_edittext);  
        //查询关键字  
        EditText editSearchKey = (EditText) findViewById(R.id.searchkey);  
        mPoiSearch.searchInCity((new PoiCitySearchOption())
        		.city(editCity.getText().toString())
        		.keyword(editSearchKey.getText().toString())
        		.pageNum(load_Index));
	}
    /** 
     * 下一组数据按钮点击事件 
     * */  
    public void goToNextPage(View v) {  
        load_Index++;  
        searchButtonProcess(null);  
    }  
   //搜路线
    public void searchButtonProcess1(View v) throws UnsupportedEncodingException {
		route=null;
		mBaiduMap.clear();
		String SearchContent = mcity.getText().toString().trim();
		String City1=URLEncoder.encode(SearchContent, "utf-8"); 
		if (City1=="") {						
			//起点
			PlanNode stNode=PlanNode.withCityNameAndPlaceName(City, start.getText().toString());
			//终点 -  把地理编码里的LatLng格式转换成PlanNode格式 
	        PlanNode endNode = PlanNode.withLocation(new LatLng(cityLng, positionLng));
	        //实际使用中对起始点城市进行正切的设定
	        if (v.getId()==R.id.driveroute) {
				//地理编码（把地址转换经纬度）
	        	mSearch.geocode(new GeoCodeOption().city(City).address(end.getText().toString()));
	        	//线路规划
	        	Search.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(endNode));      	
			}else if (v.getId()==R.id.transit) {
				//地理编码
				mSearch.geocode(new GeoCodeOption().city(City).address(end.getText().toString()));
				Search.transitSearch(new TransitRoutePlanOption().from(stNode).city(City).to(endNode)); 
			}else if (v.getId()==R.id.walkroute) {
				mSearch.geocode(new GeoCodeOption().city(City).address(end.getText().toString()));
				//线路规划不需要city
				Search.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(endNode));
			}
	        tv2.setText(start.getText().toString());
		}else if (City1!="") {
			City=City1;
			//起点
			PlanNode stNode=PlanNode.withCityNameAndPlaceName(City, start.getText().toString());
			//终点 -  把地理编码里的LatLng格式转换成PlanNode格式 
	        PlanNode endNode = PlanNode.withLocation(new LatLng(cityLng, positionLng));
	        //实际使用中对起始点城市进行正切的设定
	        if (v.getId()==R.id.driveroute) {
				//地理编码（把地址转换经纬度）
	        	mSearch.geocode(new GeoCodeOption().city(City).address(end.getText().toString()));
	        	//线路规划
	        	Search.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(endNode));      	
			}else if (v.getId()==R.id.transit) {
				//地理编码
				mSearch.geocode(new GeoCodeOption().city(City).address(end.getText().toString()));
				Search.transitSearch(new TransitRoutePlanOption().from(stNode).city(City).to(endNode)); 
			}else if (v.getId()==R.id.walkroute) {
				mSearch.geocode(new GeoCodeOption().city(City).address(end.getText().toString()));
				//线路规划不需要city
				Search.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(endNode));
			}
	        tv2.setText(start.getText().toString());
		}
	
	}
    
    
	/**
     * 定位SDK监听器，需添加locsdk jar 和so
     */
    public class MyLocationListerner implements BDLocationListener{
		private Object bitmap;
		private LatLng point;



		@Override
		public void onReceiveLocation(BDLocation location) {
			//mapview 销毁后不在处理新接收的位置 
			if (location==null||mBaiduMap==null) {
				return;
			}
			//MyLocationData.Builder定位数据建造器 
			MyLocationData locData=new MyLocationData.Builder().accuracy(location.getRadius()). // 此处设置开发者获取到的方向信息，顺时针0-360
					direction(mCurrentX).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			//设置定位数据
			mBaiduMap.setMyLocationData(locData);//			 mCurrentMode = LocationMode.NORMAL;
			
			//设置自定义图标
			MyLocationConfiguration config = new MyLocationConfiguration(mLocationMode.NORMAL, true, mIconLocation);
			    mBaiduMap.setMyLocationConfigeration(config);
			//获取经纬度  
            mLatitude = location.getLatitude();  
            mLongtitude = location.getLongitude();  
//            ll = new LatLng(mLatitude,mLongtitude);    
//            progressDialog.dismiss();
            //第一次定位的时候，那地图中心点显示为定位到的位置  
            if (isFirstLoc) {                   
                //地理坐标基本数据结构  
                LatLng loc = new LatLng(location.getLatitude(),location.getLongitude());  
                //MapStatusUpdate描述地图将要发生的变化  
                //MapStatusUpdateFactory生成地图将要反生的变化  
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(loc);  
                mBaiduMap.animateMapStatus(msu);  
                Toast.makeText(context, location.getAddrStr(),
                        Toast.LENGTH_LONG).show();
            }  
            isFirstLoc = false;  
            //显示当前定位的位置         
           City=location.getCity();
//           if (constant<pointList.size()) {
//			if (DistanceUtil.getDistance(pointList.get(constant),ll)>
//			DistanceUtil.getDistance(pointList.get(constant+1),ll)){
//				save("距离: "+DistanceUtil.getDistance(pointList.get(constant+1),ll)+" 点数: "+constant);
//				if (DistanceUtil.getDistance(pointList.get(constant+1),ll)>100&&isGetNewRoute) {
//					 IsGetNewRoute();
//				}
			}
		}
//           drawRealtimePoint(ll);
        
	


//		private void drawRealtimePoint(LatLng ll) {
//			 mBaiduMap.clear();
//		        polyline=null;
//		        MapStatus mMapStatus = new MapStatus.Builder().target(point).build();
//
//		        msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//
//
//		        overlay = new MarkerOptions().position(point)
//		                .icon((BitmapDescriptor) bitmap).zIndex(9).draggable(true);
//
//		        if (pointList.size() >=2 && pointList.size() <= 100000) {
//		            // 添加路线（轨迹）
//		            polyline = new PolylineOptions().width(10)
//		                    .color(Color.RED).points(pointList);
//		        }
//		        addMarker();
//
//			
//		}

//		private void save(String content) {
//			 try {  
//		            /* 根据用户提供的文件名，以及文件的应用模式，打开一个输出流.文件不存系统会为你创建一个的， 
//		             * 至于为什么这个地方还有FileNotFoundException抛出，我也比较纳闷。在Context中是这样定义的 
//		             *   public abstract FileOutputStream openFileOutput(String name, int mode) 
//		             *   throws FileNotFoundException; 
//		             * openFileOutput(String name, int mode); 
//		             * 第一个参数，代表文件名称，注意这里的文件名称不能包括任何的/或者/这种分隔符，只能是文件名 
//		             *          该文件会被保存在/data/data/应用名称/files/chenzheng_java.txt 
//		             * 第二个参数，代表文件的操作模式 
//		             *          MODE_PRIVATE 私有（只能创建它的应用访问） 重复写入时会文件覆盖 
//		             *          MODE_APPEND  私有   重复写入时会在文件的末尾进行追加，而不是覆盖掉原来的文件 
//		             *          MODE_WORLD_READABLE 公用  可读 
//		             *          MODE_WORLD_WRITEABLE 公用 可读写 
//		             *  */  
//		            content=content+"\n";
//		            FileOutputStream outputStream = openFileOutput("Log.log",Activity.MODE_APPEND);  
//		            outputStream.write(content.getBytes());  
//		            outputStream.flush();  
//		            outputStream.close();  
//		        } catch (FileNotFoundException e) {  
//		            e.printStackTrace();  
//		        } catch (IOException e) {  
//		            e.printStackTrace();  
//		        }  
//		}  
//	}
//    private void addMyLocation() {          
//       mBaiduMap.clear();  
//       addOverlayBtn.setEnabled(true);  
       //定义Maker坐标点    
       
       //构建Marker图标    
//       BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.arrow);    
       //构建MarkerOption，用于在地图上添加Marker    
//       OverlayOptions option = new MarkerOptions()    
//           .position(point)    
//           .icon(bitmap);    
//       //在地图上添加Marker，并显示    
//       mBaiduMap.addOverlay(option);  
//  }  
    private void init(){
		//描述地图将要发生的变化，使用工厂类MapStatusUpdateFactory创建，设置级别
        //为18，进去就是18了，默认是12
		MapStatusUpdate  mapStatusUpdate=MapStatusUpdateFactory.zoomTo(16);    
			mMapView.removeViewAt(1);//最后移除默认百度地图的logo View
			mBaiduMap.setMapStatus(mapStatusUpdate);
//			addMyLocation();		
		//是否显示缩放按钮
        mMapView.showZoomControls(false);
		//显示指南针
		mBaiduMap.getUiSettings().setCompassEnabled(true);
	} 

	
   
  public class MyBroadcastReceiver extends BroadcastReceiver {
	//实现一个广播
	@Override
	public void onReceive(Context context, Intent intent) {
		String action=intent.getAction();
		//网络错误
		if (action.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
			Toast.makeText(MainActivity.this,"无法连接网络", Toast.LENGTH_SHORT).show();
			//Key校验失败
		}else if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
			Toast.makeText(MainActivity.this,"百度地图key校验失败",Toast.LENGTH_SHORT).show();
		}

	}

}



@Override
public void onGetSuggestionResult(SuggestionResult res) {
	if (res == null || res.getAllSuggestions() == null) {  
        return;  
    }  
    sugAdapter.clear();  
    /** 
     * static class SuggestionResult.SuggestionInfo 
     * suggestion信息类 
     * */  
    for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {  
        /** 
         *  key 
         *  联想词key 
         * */  
        if (info.key != null)  
            sugAdapter.add(info.key);  
    }  
    sugAdapter.notifyDataSetChanged();  
}  
	

@Override
public void onGetPoiDetailResult(PoiDetailResult result) {
	 if (result.error != SearchResult.ERRORNO.NO_ERROR) {  
         Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();  
     } else {  
         /** 
          * public java.lang.String getName() 
          * 获取 poi 名称 
          * 返回:poi 名称 
          *  
          * public java.lang.String getAddress() 
          * 获取 poi 地址 
          * 返回:poi 地址 
          * */  
         Toast.makeText(MainActivity.this, result.getName() + ": " + result.getAddress(),   
                 Toast.LENGTH_LONG).show();  
     }  
	
}

@Override
public void onGetPoiIndoorResult(PoiIndoorResult arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public void onGetPoiResult(PoiResult result) {
	if (result==null||result.error==SearchResult.ERRORNO.ST_EN_TOO_NEAR) {
		Toast.makeText(MainActivity.this, "未找到结果", Toast.LENGTH_LONG).show();  
        return;  
	}
	sb=new StringBuilder();
	if(result.error==SearchResult.ERRORNO.NO_ERROR){
		mBaiduMap.clear();
		PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);  //这里有问题
		mBaiduMap.setOnMarkerClickListener(overlay);
		overlay.setData(result);
		overlay.addToMap();
		overlay.zoomToSpan();
		
		return;
	}
	if (result.error==SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
		String strInfo="在";
		for(CityInfo cityInfo:result.getSuggestCityList()){
			strInfo+=cityInfo.city;
			strInfo+=",";
		}
		strInfo+="找到结果";
		Toast.makeText(MainActivity.this,strInfo, Toast.LENGTH_LONG).show();
	}
}
	private class MyPoiOverlay extends PoiOverlay {
		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
			// TODO Auto-generated constructor stub
		}
		@Override
		public boolean onPoiClick(int index) {
			// TODO Auto-generated method stub
			return super.onPoiClick(index);  
	}

}

	//****************** 地理编码  ***************************

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result==null||result.error!=SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(MainActivity.this,"抱歉，请再试下", Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		 //地理编码定位 -演示时可以取消注释    (注：因为在整个demo里  点击三种路线时  它会先定位到目的地  然后才会显示出路线  用户体验缺失  所以注释掉定位.直接显示路线)
//      mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
//               .icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));
//      mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result.getLocation()));
		//得到经纬度
//		String strInfo = String.format("纬度：%f 经度：%f", result.getLocation().latitude, result.getLocation().longitude);
//      Toast.makeText(MainActivity.this, strInfo, Toast.LENGTH_SHORT).show();

        //经纬度 这里存疑
        cityLng = result.getLocation().latitude;
        positionLng = result.getLocation().longitude;
    }

	/**
	 * 节点浏览	
	 */
	public void nodeClick(View v) {
		if (route==null||
			route.getAllStep()==null){
			return;
		}
		if (nodeIndex==-1&&v.getId()==R.id.shang) {
			return;
		}
		//设置节点索引
		if (v.getId()==R.id.xia) {
			if (nodeIndex<route.getAllStep().size()-1) {
				nodeIndex++;
			}else {
				return;
			}
		}else if (v.getId()==R.id.shang) {
			if (nodeIndex>0) {
				nodeIndex--;
			}else {
				return;
			}
		}
		Object step=route.getAllStep().get(nodeIndex);
		//如果节点索引线路改变，则改变节点的位置和名称
		if (step instanceof  DrivingRouteLine.DrivingStep) {
			nodeLocation=((DrivingRouteLine.DrivingStep)step).getEntrance().getLocation();
			nodeTitle=((DrivingRouteLine.DrivingStep)step).getInstructions();
		}else if (step instanceof WalkingRouteLine.WalkingStep) {
			nodeLocation=((WalkingRouteLine.WalkingStep)step).getEntrance().getLocation();
			nodeTitle=((WalkingRouteLine.WalkingStep)step).getInstructions();
		}else if (step instanceof TransitRouteLine.TransitStep) {
			nodeLocation=((TransitRouteLine.TransitStep)step).getEntrance().getLocation();
			nodeTitle=((TransitRouteLine.TransitStep)step).getInstructions();
		}
		if (nodeLocation==null||nodeTitle==null) {
			return;
		}
		//移动到节点中心
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(nodeLocation));
		//show popup
		popupText=new TextView(MainActivity.this);
		popupText.setBackgroundResource(R.drawable.map_kuang_press1);
		popupText.setTextColor(0xFF000000);
		popupText.setText(nodeTitle);
		mBaiduMap.showInfoWindow(new InfoWindow(popupText, nodeLocation, 0));		
	}
	/**
	 * 切换路线图标，刷新地图
	 * 注意：起终点图标使用中心对齐
	 */
	public void changeRouteIcon(View v) {
		if (routeOverlay==null) {
			return;
		}
		useDefaultIcon=!useDefaultIcon;
		routeOverlay.removeFromMap();
		routeOverlay.addToMap();
	}
	
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGetBikingRouteResult(BikingRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override//驾车
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if(result==null||result.error!=SearchResult.ERRORNO.NO_ERROR){
			Toast.makeText(MainActivity.this,"抱歉，没有找到哦",Toast.LENGTH_LONG).show();
		}
		if (result.error==SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			 //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//            result.getSuggestAddrInfo();
			return;
		}
		if (result.error==SearchResult.ERRORNO.NO_ERROR) {
			nodeIndex=-1;
			route=result.getRouteLines().get(0);
			DrivingRouteOverlay overlay=new MyDrivingRouteOverlay(mBaiduMap);
			routeOverlay=overlay;
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
			SHOW1=1;
			chakan.setVisibility(View.VISIBLE);
		}
		//获取线路规划的第一条方案的线路距离  
//      int str = result. getRouteLines().get(0).getDistance(); 
	}

	@Override
	public void onGetIndoorRouteResult(IndoorRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onGetMassTransitRouteResult(MassTransitRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}
	//公交
	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            route = result.getRouteLines().get(0);
            TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            SHOW2=1;
            chakan.setVisibility(View.VISIBLE);
        }
	}
	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            route = result.getRouteLines().get(0);
            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            routeOverlay = overlay;
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
            SHOW3=1;
            chakan.setVisibility(View.VISIBLE);
        }
		
	}
	//定制驾车路线 RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay{

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}
		@Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
	}
	//定制步行路线 RouteOverly
	private class MyWalkingRouteOverlay extends WalkingRouteOverlay{

		public MyWalkingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
			// TODO Auto-generated constructor stub
		}
		 @Override
	        public BitmapDescriptor getStartMarker() {
	            if (useDefaultIcon) {
	                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
	            }
	            return null;
	        }

	        @Override
	        public BitmapDescriptor getTerminalMarker() {
	            if (useDefaultIcon) {
	                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
	            }
	            return null;
	        }		
	}
	//定制公交路线 RouteOverly
	private class MyTransitRouteOverlay extends TransitRouteOverlay {

        public MyTransitRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }
	
	public void Click(View v){	
		if (SHOW1==1||SHOW2==1||SHOW3==1) {
			chakan.setVisibility(View.VISIBLE);		
		switch (v.getId()) {
		case R.id.chakan:
			linear.setVisibility(View.VISIBLE);
            qidian.setText(start.getText().toString());
            zhongdian.setText(end.getText().toString());
            /**
             * 遍历所有节点的信息
             * 如果想展示效果就在把这个遍历注释掉,把这个事件内的注释都打开
             */
            List<String>data=new ArrayList<String>();
            for (Object step : route.getAllStep()) {
                //如果节点索引线路改变   则改变节点的位置和名称
                if (step instanceof DrivingRouteLine.DrivingStep) {
                    nodeLocation = ((DrivingRouteLine.DrivingStep) step).getEntrance().getLocation();
                    nodeTitle = ((DrivingRouteLine.DrivingStep) step).getInstructions();
                } else if (step instanceof WalkingRouteLine.WalkingStep) {
                    nodeLocation = ((WalkingRouteLine.WalkingStep) step).getEntrance().getLocation();
                    nodeTitle = ((WalkingRouteLine.WalkingStep) step).getInstructions();
                } else if (step instanceof TransitRouteLine.TransitStep) {
                    nodeLocation = ((TransitRouteLine.TransitStep) step).getEntrance().getLocation();
                    nodeTitle = ((TransitRouteLine.TransitStep) step).getInstructions();
                }
                if (nodeLocation == null || nodeTitle == null) {
                    return;
                }

                data.add(nodeTitle);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,data);
            listView.setAdapter(adapter);
            SHOW1=SHOW2=SHOW3=0;
            break;
        
		}
		}
		
	}


}