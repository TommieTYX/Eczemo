/*
Author: Loong Jian Wen
        Teh Yu Xiang
        Chua Xuan Long
 */

package team22.eczemo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class D3ActivityFragment extends Fragment {

    public D3ActivityFragment() {
    }

    // save a reference so custom methods
// can access views
    private View topLevelView;


    // save a reference to show the pie chart
    private WebView webview;

    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        boolean attachToRoot = false;
        topLevelView = inflater.inflate(
                R.layout.fragment_d3,
                container,
                attachToRoot);

        // call now or after some condition is met
        initPieChart();

        return topLevelView;
    }

    // initialize the WebView and the pie chart
    public void initPieChart() {


        View stub = topLevelView.findViewById(
                R.id.pie_chart_stub);


        if (stub instanceof ViewStub) {

            stub.setVisibility(View.VISIBLE);/*
            webview = (WebView) ((ViewStub) stub).inflate();*/

            webview = topLevelView.findViewById(
                    R.id.pie_chart_webview);


            WebSettings webSettings =
                    webview.getSettings();

            webSettings.setJavaScriptEnabled(true);

            webview.setWebChromeClient(
                    new WebChromeClient());

            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(
                        WebView view,
                        String url) {

                    // after the HTML page loads,
                    // load the pie chart
                    loadPieChart();
                }
            });

            // note the mapping from  file:///android_asset
            // to Android-D3jsPieChart/assets or
            // Android-D3jsPieChart/app/src/main/assets
           /* webview.loadUrl("file:///android_asset/" +
                    "html/piechart.html");*/
            webview.loadUrl("file:///android_asset/" +
                    "html/statistics.html");
        }
    }

    public void loadPieChart() {
        int dataset[] = new int[]{5, 10, 15, 20, 35};

        // use java.util.Arrays to format
        // the array as text
        String text = Arrays.toString(dataset);

        // pass the array to the JavaScript function
       /* webview.loadUrl("javascript:loadPieChart(" +
                text + ")");*/

    }
}
