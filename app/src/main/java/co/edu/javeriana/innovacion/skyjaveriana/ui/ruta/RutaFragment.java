package co.edu.javeriana.innovacion.skyjaveriana.ui.ruta;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import co.edu.javeriana.innovacion.skyjaveriana.R;
import co.edu.javeriana.innovacion.skyjaveriana.databinding.FragmentRutaBinding;

public class RutaFragment extends Fragment {

    private FragmentRutaBinding binding;
    TextView txt_inf_PisoO, txt_inf_PisoD, txt_inf_PisoO_E, txt_inf_PisoD_E;

    EditText etxt_PisoO, etxt_PisoD;

    Button but_darRuta;

    ImageView img_ascensor1, img_ascensor2, img_ascensor3;

    ConstraintLayout cont;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRutaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        txt_inf_PisoO = binding.txtInfPisoOrigen;
        txt_inf_PisoD = binding.txtInfPisoDest;
        txt_inf_PisoO_E = binding.txtInfPisoOrigenE;
        txt_inf_PisoD_E = binding.txtInfPisoDestE;

        etxt_PisoO = binding.etxtPisoO;
        etxt_PisoD = binding.etxtPisoD;

        but_darRuta = binding.butDarRuta;
        but_darRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                darRuta();
            }
        });

        img_ascensor1 = binding.imgAscensor1;
        img_ascensor2 = binding.imgAscensor2;
        img_ascensor3 = binding.imgAscensor3;

        cont = binding.container;
        cont.setVisibility(View.GONE);

        return root;
    }

    public void darRuta(){
        String pisoOrigen;
        String pisoDestino;

        pisoOrigen = etxt_PisoO.getText().toString();
        pisoDestino = etxt_PisoD.getText().toString();

        if(TextUtils.isEmpty(pisoOrigen) || TextUtils.isEmpty(pisoDestino))
        {
            Toast.makeText(getContext(), "Debe llenar ambos campos", Toast.LENGTH_LONG).show();
            return;
        }

        int or = Integer.parseInt(pisoOrigen);
        int des = Integer.parseInt(pisoDestino);

        if(or < 2 || or > 14 || des < 2 || des > 14)
        {
            Toast.makeText(getContext(), "Asegurese de llenar los pisos correctamente. El edificio tiene del piso 2 al 14", Toast.LENGTH_LONG).show();
            return;
        }
        cont.setVisibility(View.VISIBLE);

        int as2O = 2;
        int as2D = 8;
        int as3O = 3;
        int as3D = 12;

        int pathAO = -1;
        int pathAD = -1;

        int pathEO = -1;
        int pathED = -1;

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((3 - 1) + 1) + 1;

        switch(randomNum){
            case 1:
                pathAO = or;
                pathAD = des;
                img_ascensor1.setBackgroundTintList(getResources().getColorStateList(R.color.boxSelected, getActivity().getTheme()));
                img_ascensor2.setBackgroundTintList(getResources().getColorStateList(R.color.black, getActivity().getTheme()));
                img_ascensor3.setBackgroundTintList(getResources().getColorStateList(R.color.black, getActivity().getTheme()));
                break;
            case 2:
                if(Math.abs(or - as2D) + Math.abs(des - as2O) > Math.abs(or-as2O)+ Math.abs(des - as2D))
                {
                    pathAO = as2O;
                    pathAD = as2D;
                    if (des != as2D) {
                        pathEO = as2D;
                        pathED = des;
                    }
                }
                else
                {
                    pathAO = as2D;
                    pathAD = as2O;
                    if(des != as2O) {
                        pathEO = as2O;
                        pathED = des;
                    }
                }
                /*
                if(or == as2O || or == as2D)
                {
                    if(or == as2O) {
                        pathAO = as2O;
                        pathAD = as2D;
                        if (des != as2D) {
                            pathEO = as2D;
                            pathED = des;
                        }
                    }
                    if(or == as2D){
                        pathAO = as2D;
                        pathAD = as2O;
                        if(des != as2O) {
                            pathEO = as2O;
                            pathED = des;
                        }
                    }
                }
                else
                {
                    if(Math.abs(or - as2D) > Math.abs(or-as2O))
                    {
                        pathAO = as2O;
                        pathAD = as2D;
                        if (des != as2D) {
                            pathEO = as2D;
                            pathED = des;
                        }
                    }
                    else
                    {
                        pathAO = as2D;
                        pathAD = as2O;
                        if(des != as2O) {
                            pathEO = as2O;
                            pathED = des;
                        }
                    }
                }*/
                img_ascensor1.setBackgroundTintList(getResources().getColorStateList(R.color.black, getActivity().getTheme()));
                img_ascensor2.setBackgroundTintList(getResources().getColorStateList(R.color.boxSelected, getActivity().getTheme()));
                img_ascensor3.setBackgroundTintList(getResources().getColorStateList(R.color.black, getActivity().getTheme()));
                break;
            case 3:
                if(Math.abs(or - as3D) + Math.abs(des - as3O)> Math.abs(or-as3O) + Math.abs(des-as3D))
                {
                    pathAO = as3O;
                    pathAD = as3D;
                    if (des != as3D) {
                        pathEO = as3D;
                        pathED = des;
                    }
                }
                else
                {
                    pathAO = as3D;
                    pathAD = as3O;
                    if(des != as3O) {
                        pathEO = as3O;
                        pathED = des;
                    }
                }
                /*
                 if(or == as3O || or == as3D)
                {
                    if(or == as3O) {
                        pathAO = as3O;
                        pathAD = as3D;
                        if (des != as3D) {
                            pathEO = as3D;
                            pathED = des;
                        }
                    }
                    if(or == as3D){
                        pathAO = as3D;
                        pathAD = as3O;
                        if(des != as3O) {
                            pathEO = as3O;
                            pathED = des;
                        }
                    }
                }
                else
                {
                    if(Math.abs(or - as3D) > Math.abs(or-as3O))
                    {
                        pathAO = as3O;
                        pathAD = as3D;
                        if (des != as3D) {
                            pathEO = as3D;
                            pathED = des;
                        }
                    }
                    else
                    {
                        pathAO = as3D;
                        pathAD = as3O;
                        if(des != as3O) {
                            pathEO = as3O;
                            pathED = des;
                        }
                    }
                }
                 */
                img_ascensor1.setBackgroundTintList(getResources().getColorStateList(R.color.black, getActivity().getTheme()));
                img_ascensor2.setBackgroundTintList(getResources().getColorStateList(R.color.black, getActivity().getTheme()));
                img_ascensor3.setBackgroundTintList(getResources().getColorStateList(R.color.boxSelected, getActivity().getTheme()));
                break;
        }

        txt_inf_PisoO.setText(""+pathAO);
        txt_inf_PisoD.setText(""+pathAD);

        if(pathEO != -1 && pathED != -1)
        {
            txt_inf_PisoO_E.setText(""+pathEO);
            txt_inf_PisoD_E.setText(""+pathED);
        }
        else
        {
            txt_inf_PisoO_E.setText("----");
            txt_inf_PisoD_E.setText("----");
        }

    }


}