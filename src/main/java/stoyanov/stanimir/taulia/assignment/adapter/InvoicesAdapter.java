package stoyanov.stanimir.taulia.assignment.adapter;

import stoyanov.stanimir.taulia.assignment.model.BuyerEntry;
import stoyanov.stanimir.taulia.assignment.model.BuyersList;
import stoyanov.stanimir.taulia.assignment.model.Invoice;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InvoicesAdapter extends XmlAdapter<BuyersList, Map<String, List<Invoice>>> {
    @Override
    public Map<String, List<Invoice>> unmarshal(BuyersList v) throws Exception {
        return v.getList()
                .stream()
                .collect(
                        Collectors.toMap(
                                BuyerEntry::getKey, BuyerEntry::getList
                        ));
    }

    @Override
    public BuyersList marshal(Map<String, List<Invoice>> v) throws Exception {
        var list =  v.entrySet()
                .stream()
                .map(
                        stringListEntry -> new BuyerEntry(stringListEntry.getKey(), stringListEntry.getValue())
                ).toList();
        return new BuyersList(list);
    }
}
