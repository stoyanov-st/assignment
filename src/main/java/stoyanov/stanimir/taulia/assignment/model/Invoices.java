package stoyanov.stanimir.taulia.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import stoyanov.stanimir.taulia.assignment.adapter.InvoicesAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Invoices")
@XmlAccessorType(XmlAccessType.FIELD)
public class Invoices {

    @XmlJavaTypeAdapter(InvoicesAdapter.class)
    @XmlElement(name = "Buyers")
    private Map<String, List<Invoice>> buyers = new HashMap<>();
}

