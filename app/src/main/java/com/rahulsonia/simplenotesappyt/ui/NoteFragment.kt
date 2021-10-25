package com.rahulsonia.simplenotesappyt.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rahulsonia.simplenotesappyt.R
import com.rahulsonia.simplenotesappyt.adapter.NoteAdapter
import com.rahulsonia.simplenotesappyt.data.entity.Note
import com.rahulsonia.simplenotesappyt.databinding.FragmentNotesBinding
import com.rahulsonia.simplenotesappyt.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteFragment: Fragment(R.layout.fragment_notes), NoteAdapter.OnNoteClickListener {

    val viewModel by viewModels<NoteViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = FragmentNotesBinding.bind(requireView())

        binding.apply {
            recyclerViewNotes.layoutManager = GridLayoutManager(context, 2)
            recyclerViewNotes.setHasFixedSize(true)

            addBtn.setOnClickListener {
                val action = NoteFragmentDirections.actionNoteFragmentToAddEditNoteFragment(null)
                findNavController().navigate(action)
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notes.collect {notes ->
                    val adapter = NoteAdapter(notes, this@NoteFragment)
                    recyclerViewNotes.adapter = adapter
                }
            }
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notesEvent.collect {event ->
                    if(event is NoteViewModel.NotesEvent.ShowUndoSnackBar){
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).setAction("UNDO"){
                            viewModel.insertNote(event.note)
                        }.show()
                    }
                }
            }
        }
    }

    override fun onNoteClick(note: Note) {
        val action = NoteFragmentDirections.actionNoteFragmentToAddEditNoteFragment(note)
        findNavController().navigate(action)
    }

    override fun onNoteLongClick(note: Note) {
        viewModel.deleteNote(note)
    }
}









