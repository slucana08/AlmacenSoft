/*
        Copyright 2017 BrotherV

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/

package com.dms.almacensoft.features.principal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.bvapp.arcmenulibrary.ArcMenu;
import com.bvapp.arcmenulibrary.widget.FloatingActionButton;
import com.dms.almacensoft.R;
import com.dms.almacensoft.features.configuracion.ConfiguracionActivity;
import com.dms.almacensoft.features.shared.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GeneralActivityOld extends BaseActivity {

    @BindView(R.id.general_image_view)
    ImageView generalImageView;
    @BindView(R.id.layer_image_view)
    ImageView layerImageView;

    int cover = 0;

    private static final int[] itemDrawables = {R.drawable.ic_trolley,
            R.drawable.ic_inventory, R.drawable.ic_delivery, R.drawable.ic_picking,
            R.drawable.ic_package, R.drawable.ic_cargo_truck,R.drawable.ic_printer,
            R.drawable.ic_conveyor, R.drawable.ic_menu_settings_white} ;

    private static final String[] str = {"Recepción", "Inventario", "Despacho",
        "Picking" , "Packing", "Transporte","Impresión", "Mov. Prod", "Ajustes"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_old);

        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        ArcMenu menu = findViewById(R.id.general_arc_menu);
        menu.showTooltip(true);
        menu.setToolTipBackColor(Color.WHITE);
        menu.setToolTipCorner(6f);
        menu.setToolTipPadding(2.5f);
        menu.setToolTipTextSize(13);
        menu.setToolTipTextColor(R.color.colorPrimaryDark);
        menu.setAnim(300, 300, ArcMenu.ANIM_MIDDLE_TO_RIGHT, ArcMenu.ANIM_MIDDLE_TO_RIGHT,
                ArcMenu.ANIM_INTERPOLATOR_ACCELERATE_DECLERATE, ArcMenu.ANIM_INTERPOLATOR_ACCELERATE_DECLERATE);
        menu.setOnClickListener(v -> {
            if (cover == 0) {
                layerImageView.setBackgroundColor(175 * 0x1000000);
                cover = 1;
            } else {
                layerImageView.setBackgroundColor(0 * 0x1000000);
                cover = 0;
            }
        });

        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            FloatingActionButton item = new FloatingActionButton(this);  // Use internal FAB as child
            // ********* import com.bvapp.arcmenulibrary.widget.FloatingActionButton; *********

            item.setSize(70); // set initial size for child, it will create fab first
            item.setIcon(itemDrawables[i]); // It will set fab icon from your resources which related to 'ITEM_DRAWABLES'
            item.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));// it will set fab child's color
            menu.setChildSize(item.getIntrinsicHeight()); // set absolout child size for menu

            final int position = i;
            menu.addItem(item, str[i], new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (position == 8) {
                                startActivity(new Intent(GeneralActivityOld.this, ConfiguracionActivity.class));
                                finish();
                            }
                        }
                    },200);


                    if (((FloatingActionButton) v).getIcon() == ContextCompat.getDrawable(GeneralActivityOld.this, R.drawable.ic_menu_settings_white)){
                        startActivity(new Intent(GeneralActivityOld.this, ConfiguracionActivity.class));
                        finish();
                    }
                }
            });
        }


    }
}


