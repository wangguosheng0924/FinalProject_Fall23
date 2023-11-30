package algonquin.cst2335.finalproject_fall23;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private ArrayList<String> savedWordList;

    public HistoryFragment(ArrayList<String> savedWordList) {
        this.savedWordList = savedWordList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        // version1: there is only one textview in the layout. Access views in the fragment layout and display the content
//        TextView historyTextView = view.findViewById(R.id.textViewForSavedWordList);
//        StringBuilder historyText = new StringBuilder();
//
//        for (String word : savedWordList) {
//            historyText.append(word).append("\n");
//        }
//        historyTextView.setText(historyText.toString());

        // version 2: there is a listview that contains the saved words.
        ListView historyListView = view.findViewById(R.id.listViewForSavedWordList);

        // Set up an ArrayAdapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, savedWordList);
        historyListView.setAdapter(adapter);

        // Set a click listener for items in the ListView
        historyListView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedWord = savedWordList.get(position);

            // TODO: Implement logic to display the definition for the selected word
            // You can use a dialog, Snackbar, or another fragment to display the definition
            // For example, you might want to retrieve the definition from the database using the selectedWord
            // and then display it in a dialog.

            // For demonstration, let's display the selected word in a Toast
            Toast.makeText(getContext(), "Selected Word: " + selectedWord, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
