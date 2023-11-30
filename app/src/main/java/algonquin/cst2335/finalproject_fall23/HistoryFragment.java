package algonquin.cst2335.finalproject_fall23;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import algonquin.cst2335.finalproject_fall23.databinding.DetailFragmentLayoutBinding;
import algonquin.cst2335.finalproject_fall23.databinding.FragmentLayoutBinding;
import algonquin.cst2335.finalproject_fall23.databinding.PostsearchlayoutBinding;
import algonquin.cst2335.finalproject_fall23.DictionaryActivity;

public class HistoryFragment extends Fragment {
    private ArrayList<String> savedWordList;

    ArrayList<Definition> definitionsLocal;

    RecyclerView.Adapter myAdapter;

    DefinitionViewModel definitionModel;

    DefinitionDAO mDAO;

    FragmentLayoutBinding binding;
    PostsearchlayoutBinding binding1;

    DetailFragmentLayoutBinding bindingDetail;

    public HistoryFragment(ArrayList<String> savedWordList) {
        this.savedWordList = savedWordList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        ListView historyListView = view.findViewById(R.id.listViewForSavedWordList);

        // Set up an ArrayAdapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, savedWordList);
        historyListView.setAdapter(adapter);

        // Set a click listener for items in the ListView
        historyListView.setOnItemClickListener((parent, view1, position, id) -> {

            String selectedWord = savedWordList.get(position);
            Toast.makeText(getContext(), "Selected Word: " + selectedWord, Toast.LENGTH_SHORT).show();
//            String saved_definition = null;
//            String coresDef = mDAO.searchDefinitions(selectedWord).toString();
////            for (Definition item : definitionsLocal) {
////                saved_definition = item.getDefinition();
////            }

            DetailFragment detailFragment = DetailFragment.newInstance(selectedWord);
            // Replace the current layout with the HistoryFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.detailFrameLayout, detailFragment)
                    .addToBackStack(null)
                    .commit();
            Toast.makeText(getContext(), "Selected Word: " + selectedWord, Toast.LENGTH_SHORT).show();
        });

//        // version 3: use recyclerview to contain the words
//        RecyclerView historyRecyclerView =  view.findViewById(R.id.recyclerViewForSavedWords);
//        // the items in the recyclerView can be arranged either vertical or horizontal.
//        binding.recyclerViewForSavedWords.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // setAdapter for recyclerView
//        binding.recyclerViewForSavedWords.setAdapter(myAdapter= new RecyclerView.Adapter<DictionaryActivity.MyRowHolder>() {
//            @NonNull
//            @Override
//            //adapter is like a bridge to create a new view-myrowholder
//            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                binding1 = PostsearchlayoutBinding.inflate(getLayoutInflater());
//                return new MyRowHolder(binding1.getRoot()); // put binding1 into this viewHolder for items.
//            }
//            @Override
//            //adapter is like a bridge to bind data into the viewholder-myrowholder in the recyclerview.
//            public void onBindViewHolder(@NonNull DictionaryActivity.MyRowHolder holder, int position) {
//                Definition currentDefinition = definitionsLocal.get(position);
//                holder.searched_term.setText(currentDefinition.getTerm());
//                holder.def1.setText(currentDefinition.getDefinition());
//            }
//            @Override
//            public int getItemCount() {
//                return definitionsLocal != null ? definitionsLocal.size() : 0;
//            }
//        });// end of setAdapter
        return view;
    }
//
//    //myRowHolder
//    class MyRowHolder extends RecyclerView.ViewHolder {
//        TextView searched_term;
//        TextView def1;
//        public MyRowHolder(@NonNull View itemView) {
//            super(itemView);
//            searched_term=itemView.findViewById(R.id.searched_term);
//            // 实现你的数据集合以及创建和绑定视图项的逻辑
//            def1=itemView.findViewById(R.id.def1);
////            def2=itemView.findViewById(R.id.def2);
////            def3=itemView.findViewById(R.id.def3);
//
//            itemView.setOnClickListener(clk -> {
//                int position = getAdapterPosition();
//                Definition selectedTerm = definitionsLocal.get(position);
//                definitionModel.selectedDefinition.postValue(selectedTerm);
//                AlertDialog.Builder builder = new AlertDialog.Builder( DictionaryActivity.this );
//                builder.setMessage("Do you want to delete the message:"+ selectedTerm.getTerm());
//                builder.setTitle("Question:");
//                builder.setNegativeButton("No",(dialog,cl)->{});
//                builder.setPositiveButton("Yes",(dialog,cl)->{
//                    definitionsLocal.remove(position);
//                    myAdapter.notifyItemRemoved(position);
//                    Snackbar.make(searched_term,"You deleted message #"+ position, Snackbar.LENGTH_LONG).setAction("Undo", click->{
//                        definitionsLocal.add(position, selectedTerm);
//                        myAdapter.notifyItemInserted(position);
//                    }).show();
//                });
//                builder.create().show();
//            });
//        }
//    } // end of MyRowHolder
    }// end of fragment class

