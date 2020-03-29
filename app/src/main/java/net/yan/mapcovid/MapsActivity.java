package net.yan.mapcovid;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Covid objetoCovid;
    private List<String> lista = new ArrayList<>();
    public Alt alt;
    public Dados dados;
    private Map<Marker, Map<String, Object>> markers = new HashMap<>();
    private Map<String, Covid> dataModel = new HashMap<>();
    public NestedScrollView mCustomBottomSheet;
    private BottomSheetBehavior<NestedScrollView> mBottomSheetBehavior;
    private LinearLayout mHeaderLayout;

    public TextView country;
    public TextView casos;
    public TextView casosH;
    public TextView mortes;
    public TextView mortesh;
    public TextView prec;
    public TextView pcrit;
    public TextView casosA;
    public TextView casos_p_mil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mCustomBottomSheet = findViewById(R.id.custom_bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mCustomBottomSheet);
        mHeaderLayout = findViewById(R.id.header_layout);
        country = mCustomBottomSheet.findViewById(R.id.country);
        casosH = mCustomBottomSheet.findViewById(R.id.casesToday);
        casos = mCustomBottomSheet.findViewById(R.id.casos);
        casosA = mCustomBottomSheet.findViewById(R.id.actives);
        pcrit = mCustomBottomSheet.findViewById(R.id.critical);
        casos_p_mil = mCustomBottomSheet.findViewById(R.id.p_million);
        mortes = mCustomBottomSheet.findViewById(R.id.mortes);
        mortesh = mCustomBottomSheet.findViewById(R.id.mortesToday);
        prec = mCustomBottomSheet.findViewById(R.id.recuperados);
        //mHeaderImage = findViewById(R.id.header_arrow);
    }


    public interface Alt {
        public void valor(List<String> lista, Map<String, Covid> dataModel);
    }
    public interface Dados {
        public void val(Covid covid);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
        restApi();
        updateApi(new Alt() {
            public Map<String, Covid> listaa = new HashMap<>();
            public List<String> paises = new ArrayList<>();

            @Override
            public void valor(final List<String> lista, final Map<String, Covid> dataModel) {
                listaa = dataModel;
                paises = lista;
                for (int i = 0; i < dataModel.size(); i++) {
                    objetoCovid = new Covid();
                    objetoCovid = dataModel.get(lista.get(i));
                    LatLng casa = new LatLng(Double.parseDouble(objetoCovid.getLat()), Double.parseDouble(objetoCovid.getLongi()));
                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(casa);
                    //MÁXIMO DE 1.000.000 DE METROS
                    boolean teste = false;
                    if (Integer.parseInt(objetoCovid.getCases()) > 20000) {
                        circleOptions.radius(1000000);
                        circleOptions.fillColor(Color.argb(50, 230, 0, 0));
                        teste = true;
                    }
                    circleOptions.strokeWidth(0);
                    circleOptions.clickable(true);
                    circleOptions.strokeColor(Color.TRANSPARENT);
                    mMap.addCircle(circleOptions);
                    mMap.addMarker(new MarkerOptions()
                            .position(casa)
                            .title(objetoCovid.getCoutry())
                            //   .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
                }
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Covid covid = dataModel.get(marker.getTitle());
                        bottomSheet(covid);
                    }
                });
            }
        });

    }

    public void teste(Dados listener) {
        dados = listener;
    }

    public void updateApi(Alt listener) {
        alt = listener;
    }

    public void restApi() {
        RequestQueue r = Volley.newRequestQueue(getApplicationContext());
        String url2 = "https://corona.lmao.ninja/countries/";
        String url = "https://coronavirus-tracker-api.herokuapp.com/v2/locations";
        StringRequest request = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                JSONObject ob2 = object.getJSONObject("countryInfo");
                                objetoCovid = new Covid();
                                objetoCovid.setLat(ob2.getString("lat"));
                                objetoCovid.setLongi(ob2.getString("long"));
                                objetoCovid.setCases(object.getString("cases"));
                                objetoCovid.setCoutry(object.getString("country"));
                                objetoCovid.setCritical(object.getString("critical"));
                                objetoCovid.setRecovery(object.getString("recovered"));
                                objetoCovid.setTodayCase(object.getString("todayCases"));
                                objetoCovid.setDeaths(object.getString("deaths"));
                                objetoCovid.setTodayDeaths(object.getString("todayDeaths"));
                                objetoCovid.setActive(object.getString("active"));
                                objetoCovid.setpMillions(object.getString("casesPerOneMillion"));
                                dataModel.put(objetoCovid.getCoutry(), objetoCovid);
                                lista.add(objetoCovid.getCoutry());
                            }
                            alt.valor(lista, dataModel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VALORES", error.getMessage());
            }
        });
        r.add(request);
    }
    public void bottomSheet(Covid covid){
        country.setText(covid.getCoutry());
        casos.setText("Casos - "+covid.getCases());
        casosH.setText("Casos Hoje - "+covid.getTodayCase());
        mortes.setText("Mortes - "+covid.getDeaths());
        mortesh.setText("Mortes Hoje - "+covid.getTodayDeaths());
        prec.setText("Recuperados - "+covid.getRecovery());
        pcrit.setText("Em estado grave - "+covid.getCritical());
        casosA.setText("Casos Ativos - "+ covid.getActive());
        casos_p_mil.setText( "Casos Por milhão - "+covid.getpMillions());
        if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }



    public void rejeitos(){
        //  Fragmento openBottomSheet = Fragmento.newInstance();
        // openBottomSheet.show(getSupportFragmentManager(), Fragmento.TAG);

        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);



        /*
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //ÁREA CIRCULADA

                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(latLng);
                //MÁXIMO DE 1.000.000 DE METROS
                circleOptions.radius(1000000);
                circleOptions.strokeWidth(0);
                circleOptions.clickable(true);
                circleOptions.strokeColor(Color.TRANSPARENT);
                circleOptions.fillColor(Color.argb(40, 226, 199, 3));
                mMap.addCircle(circleOptions);


                //ADICIONANDO LINHAS
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add(casa);
                polylineOptions.add(latLng);
                polylineOptions.color(Color.GREEN);
                polylineOptions.width(5);
                mMap.addPolyline(polylineOptions);

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("LOCAL")
                        .snippet("descrição do local")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.carro)));
                //ZOOM DE 2.0 ATÉ 21.0
                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4));
            }
        });

         */
/*
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        //        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.carro)));

        //ZOOM DE 2.0 ATÉ 21.0
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
 */
    }
}
