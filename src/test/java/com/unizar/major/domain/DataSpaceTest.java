package com.unizar.major.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DataSpaceTest {

    private DataSpace dataSpace;

    @Before
    public void generateDataSpace() {
        dataSpace = new DataSpace("betan", 1);
    }

    @Test
    public void edificio() {
        assertEquals("betan", dataSpace.getEdificio());
        dataSpace.setEdificio("ada");
        assertEquals("ada", dataSpace.getEdificio());
    }

    @Test
    public void planta() {
        assertEquals(1, dataSpace.getPlanta());
        dataSpace.setPlanta(-1);
        assertEquals(-1, dataSpace.getPlanta());
    }

    @Test
    public void emptyBuilder() {
        dataSpace = new DataSpace();
        assertNull(dataSpace.getEdificio());
        assertEquals(0, dataSpace.getPlanta());
    }

}