package spwrap.component;

import spwrap.annotations.StoredProc;

import java.util.List;

public interface ComponentDAO {

    @StoredProc("fill_component")
    void fillComponent();


    @StoredProc("list_components")
    List<Component> listComponents();
}
