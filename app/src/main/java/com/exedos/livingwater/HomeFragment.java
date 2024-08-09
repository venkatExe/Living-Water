package com.exedos.livingwater;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class HomeFragment extends Fragment {

//    private TextView quantityTextView;
//    private int quantity = 1;
private CardView waterCardView;
    private ImageView waterImage;
    private TextView waterTitle;
    private Button decreaseButton;
    private TextView quantityTextView;
    private Button increaseButton;
    private Button orderButton;
    private int waterQuantity = 1;

    // Variables for the second CardView (c_waterCardView)
    private CardView cWaterCardView;
    private ImageView cWaterImage;
    private TextView cWaterTitle;
    private Button cDecreaseButton;
    private TextView cQuantityTextView;
    private Button cIncreaseButton;
    private Button cOrderButton;
    private int cWaterQuantity = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
//        quantityTextView = rootView.findViewById(R.id.quantityTextView);
//        // for normal water cans
//        Button decreaseButton = rootView.findViewById(R.id.decreaseButton);
//        Button increaseButton = rootView.findViewById(R.id.increaseButton);
//        Button orderButton = rootView.findViewById(R.id.orderButton);
//
//
//        // for cooling water cans
//        Button c_decreaseButton = rootView.findViewById(R.id.c_decreaseButton);
//        Button c_increaseButton = rootView.findViewById(R.id.c_increaseButton);
//        Button c_orderButton = rootView.findViewById(R.id.c_orderButton);
//
//
//
//
//        decreaseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                decreaseQuantity();
//            }
//        });
//
//        increaseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                increaseQuantity();
//            }
//        });
//
//        orderButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Add logic to handle the order button click
//                // For example, you can open a new activity or show a confirmation dialog.
//            }
//        });
//
//
//        c_decreaseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                decreaseQuantity();
//            }
//        });
//
//        c_increaseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                increaseQuantity();
//            }
//        });
//
//        c_orderButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Add logic to handle the order button click
//                // For example, you can open a new activity or show a confirmation dialog.
//            }
//        });
//
//        return rootView;
//    }
//
//
//
//
//    public void decreaseQuantity() {
//        if (quantity > 1) {
//            quantity--;
//            quantityTextView.setText(String.valueOf(quantity));
//        }
//    }
//
//    public void increaseQuantity() {
//        quantity++;
//        quantityTextView.setText(String.valueOf(quantity));
//    }
//
//    // If you need to handle the "order" action differently for this fragment, add it here.
//}
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the UI elements for the first CardView
        waterCardView = rootView.findViewById(R.id.waterCardView);
        waterImage = rootView.findViewById(R.id.waterImage);
        waterTitle = rootView.findViewById(R.id.waterTitle);
        decreaseButton = rootView.findViewById(R.id.decreaseButton);
        quantityTextView = rootView.findViewById(R.id.quantityTextView);
        increaseButton = rootView.findViewById(R.id.increaseButton);
        orderButton = rootView.findViewById(R.id.orderButton);

        // Set click listeners for the first CardView
        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseWaterQuantity();
            }
        });

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseWaterQuantity();
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleWaterOrder();
            }
        });

        // Initialize the UI elements for the second CardView (c_waterCardView)
        cWaterCardView = rootView.findViewById(R.id.c_waterCardView);
        cWaterImage = rootView.findViewById(R.id.c_waterImage);
        cWaterTitle = rootView.findViewById(R.id.c_waterTitle);
        cDecreaseButton = rootView.findViewById(R.id.c_decreaseButton);
        cQuantityTextView = rootView.findViewById(R.id.c_quantityTextView);
        cIncreaseButton = rootView.findViewById(R.id.c_increaseButton);
        cOrderButton = rootView.findViewById(R.id.c_orderButton);

        // Set click listeners for the second CardView (c_waterCardView)
        cDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseCWaterQuantity();
            }
        });

        cIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseCWaterQuantity();
            }
        });

        cOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCWaterOrder();
            }
        });

        return rootView;
    }

    // Methods for the first CardView (waterCardView)
    private void decreaseWaterQuantity() {
        if (waterQuantity > 1) {
            waterQuantity--;
            quantityTextView.setText(String.valueOf(waterQuantity));
        }
    }

    private void increaseWaterQuantity() {
        waterQuantity++;
        quantityTextView.setText(String.valueOf(waterQuantity));
    }

    private void handleWaterOrder() {
        // Handle the order for the first CardView
        // Implement the desired order logic here
    }

    // Methods for the second CardView (c_waterCardView)
    private void decreaseCWaterQuantity() {
        if (cWaterQuantity > 1) {
            cWaterQuantity--;
            cQuantityTextView.setText(String.valueOf(cWaterQuantity));
        }
    }

    private void increaseCWaterQuantity() {
        cWaterQuantity++;
        cQuantityTextView.setText(String.valueOf(cWaterQuantity));
    }

    private void handleCWaterOrder() {
        // Handle the order for the second CardView (c_waterCardView)
        // Implement the desired order logic here


    }
}
