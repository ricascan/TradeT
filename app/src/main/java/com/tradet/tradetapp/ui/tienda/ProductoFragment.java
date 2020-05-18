package com.tradet.tradetapp.ui.tienda;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tradet.tradetapp.Producto;
import com.tradet.tradetapp.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductoFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Producto producto;
    private MapView mapView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProductoFragment() {
        // Required empty public constructor
    }

    public ProductoFragment(Producto producto){
        this.producto = producto;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductoFragment newInstance(String param1, String param2) {
        ProductoFragment fragment = new ProductoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_producto, container, false);

        ImageView image;
        TextView nombre, descripcion, precio, usuario, contacto;

        image = root.findViewById(R.id.imageViewProducto);
        nombre = root.findViewById(R.id.productoNombreTextView);
        descripcion = root.findViewById(R.id.productoDescripcionTextView);
        precio = root.findViewById(R.id.productoPrecioTextView);
        usuario = root.findViewById(R.id.productoUsuarioTextVIew);
        contacto = root.findViewById(R.id.productoContactoTextView);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map2);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        Bitmap bitmap = BitmapFactory.decodeByteArray(producto.getFoto(), 0, producto.getFoto().length);

        image.setImageBitmap(bitmap);
        nombre.setText("Nombre del producto: " + producto.getNombre());
        descripcion.setText("Descripción del producto: \n\n" + producto.getDescripcion());
        precio.setText("Precio del producto: " + producto.getPrecio() + " €");
        usuario.setText("Vendedor: " + producto.getUsuario().getNombre());
        contacto.setText("Contacto: \n\t- " + producto.getUsuario().getEmail()+"\n\t- "+producto.getUsuario().getTelefono());


        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng ubicacionProducto = new LatLng(producto.getLatitud(), producto.getLongitud());
        googleMap.addMarker(new MarkerOptions().position(ubicacionProducto).title("Ubicación del producto"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacionProducto));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
