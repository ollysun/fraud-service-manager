package com.etz.fraudeagleeyemanager.dto.request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CreateParameterRequestTest {
    @Test
    public void testCanEqual() {
        assertFalse((new CreateParameterRequest()).canEqual("Other"));
    }

    @Test
    public void testCanEqual2() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        assertTrue(createParameterRequest.canEqual(new CreateParameterRequest()));
    }

    @Test
    public void testConstructor() {
        CreateParameterRequest actualCreateParameterRequest = new CreateParameterRequest();
        actualCreateParameterRequest.setAuthorised(true);
        actualCreateParameterRequest.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        actualCreateParameterRequest.setName("Name");
        actualCreateParameterRequest.setOperator("Operator");
        actualCreateParameterRequest.setRequireValue(true);
        assertTrue(actualCreateParameterRequest.getAuthorised());
        assertEquals("Jan 1, 2020 8:00am GMT+0100", actualCreateParameterRequest.getCreatedBy());
        assertEquals("Name", actualCreateParameterRequest.getName());
        assertEquals("Operator", actualCreateParameterRequest.getOperator());
        assertTrue(actualCreateParameterRequest.getRequireValue());
        assertEquals("CreateParameterRequest(name=Name, operator=Operator, requireValue=true, createdBy=Jan 1, 2020 8:00am"
                + " GMT+0100, authorised=true)", actualCreateParameterRequest.toString());
    }

    @Test
    public void testEquals() {
        assertFalse((new CreateParameterRequest()).equals("42"));
    }

    @Test
    public void testEquals10() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setRequireValue(true);

        CreateParameterRequest createParameterRequest1 = new CreateParameterRequest();
        createParameterRequest1.setRequireValue(true);
        assertTrue(createParameterRequest.equals(createParameterRequest1));
    }

    @Test
    public void testEquals11() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setAuthorised(true);

        CreateParameterRequest createParameterRequest1 = new CreateParameterRequest();
        createParameterRequest1.setAuthorised(true);
        assertTrue(createParameterRequest.equals(createParameterRequest1));
    }

    @Test
    public void testEquals12() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setOperator("Jan 1, 2020 8:00am GMT+0100");
        createParameterRequest.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");

        CreateParameterRequest createParameterRequest1 = new CreateParameterRequest();
        createParameterRequest1.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        assertFalse(createParameterRequest.equals(createParameterRequest1));
    }

    @Test
    public void testEquals13() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setName("Jan 1, 2020 8:00am GMT+0100");
        createParameterRequest.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");

        CreateParameterRequest createParameterRequest1 = new CreateParameterRequest();
        createParameterRequest1.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        assertFalse(createParameterRequest.equals(createParameterRequest1));
    }

    @Test
    public void testEquals14() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");

        CreateParameterRequest createParameterRequest1 = new CreateParameterRequest();
        createParameterRequest1.setOperator("Jan 1, 2020 8:00am GMT+0100");
        createParameterRequest1.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        assertFalse(createParameterRequest.equals(createParameterRequest1));
    }

    @Test
    public void testEquals15() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");

        CreateParameterRequest createParameterRequest1 = new CreateParameterRequest();
        createParameterRequest1.setName("Jan 1, 2020 8:00am GMT+0100");
        createParameterRequest1.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        assertFalse(createParameterRequest.equals(createParameterRequest1));
    }

    @Test
    public void testEquals2() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        assertTrue(createParameterRequest.equals(new CreateParameterRequest()));
    }

    @Test
    public void testEquals3() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        assertFalse(createParameterRequest.equals(new CreateParameterRequest()));
    }

    @Test
    public void testEquals4() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setRequireValue(true);
        assertFalse(createParameterRequest.equals(new CreateParameterRequest()));
    }

    @Test
    public void testEquals5() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setAuthorised(true);
        assertFalse(createParameterRequest.equals(new CreateParameterRequest()));
    }

    @Test
    public void testEquals6() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();

        CreateParameterRequest createParameterRequest1 = new CreateParameterRequest();
        createParameterRequest1.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        assertFalse(createParameterRequest.equals(createParameterRequest1));
    }

    @Test
    public void testEquals7() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();

        CreateParameterRequest createParameterRequest1 = new CreateParameterRequest();
        createParameterRequest1.setRequireValue(true);
        assertFalse(createParameterRequest.equals(createParameterRequest1));
    }

    @Test
    public void testEquals8() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();

        CreateParameterRequest createParameterRequest1 = new CreateParameterRequest();
        createParameterRequest1.setAuthorised(true);
        assertFalse(createParameterRequest.equals(createParameterRequest1));
    }

    @Test
    public void testEquals9() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");

        CreateParameterRequest createParameterRequest1 = new CreateParameterRequest();
        createParameterRequest1.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        assertTrue(createParameterRequest.equals(createParameterRequest1));
    }

    @Test
    public void testHashCode() {
        assertEquals(1244954382, (new CreateParameterRequest()).hashCode());
    }

    @Test
    public void testHashCode2() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        assertEquals(-2067636181, createParameterRequest.hashCode());
    }

    @Test
    public void testHashCode3() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setRequireValue(true);
        assertEquals(-1539489934, createParameterRequest.hashCode());
    }

    @Test
    public void testHashCode4() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setAuthorised(true);
        assertEquals(1488944634, createParameterRequest.hashCode());
    }

    @Test
    public void testHashCode5() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setOperator("Operator");
        assertEquals(1296098897, createParameterRequest.hashCode());
    }

    @Test
    public void testHashCode6() {
        CreateParameterRequest createParameterRequest = new CreateParameterRequest();
        createParameterRequest.setName("Name");
        assertEquals(1080265102, createParameterRequest.hashCode());
    }
}

