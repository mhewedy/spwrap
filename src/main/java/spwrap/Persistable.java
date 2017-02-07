package spwrap;

import java.util.List;

import spwrap.Executor.Param;

public interface Persistable {

	List<Param> toInputParams();
}