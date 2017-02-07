package spwrap;

import java.util.List;

import spwrap.Caller.Param;

public interface Persistable {

	List<Param> toInputParams();
}