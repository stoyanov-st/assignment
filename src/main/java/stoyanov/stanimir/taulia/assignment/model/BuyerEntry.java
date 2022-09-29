package stoyanov.stanimir.taulia.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class BuyerEntry {

    @XmlElement(name = "Name")
    private String key;

    @XmlElement(name = "Invoice")
    private List<Invoice> list = new ArrayList<>();
}

