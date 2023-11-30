package algonquin.cst2335.finalproject_fall23;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class DetailFragment extends Fragment {
    String savedDefinition;

    ArrayList<Definition> definitionsLocal;

    RecyclerView.Adapter myAdapter;

    DefinitionViewModel definitionModel;

    DefinitionDAO mDAO;

    private String selectedWord;

    public DetailFragment() {
        //this.savedDefinition=savedDefinition;
    }

    public static DetailFragment newInstance(String selectedWord){
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("selectedWord", selectedWord);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedWord = getArguments().getString("selectedWord");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment_layout, container, false);

        // TODO: Fetch and display the definition for the selectedWord
        // You can use the selectedWord to retrieve the definition from the database or another source.
//        TextView textView =view.findViewById(R.id.defTextView);
//        String saved_definition = null;
//        String coresDef = mDAO.searchDefinitions(selectedWord).toString();
//            for (Definition item : definitionsLocal) {
//                saved_definition = item.getDefinition();
//            }

        return view;
    }
}
