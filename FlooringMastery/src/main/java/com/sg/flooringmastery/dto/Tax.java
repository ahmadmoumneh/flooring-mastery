package com.sg.flooringmastery.dto;

import java.math.BigDecimal;
import static com.sg.flooringmastery.dao.FlooringMasteryTaxDaoFileImpl.states;
import java.util.Objects;

/**
 *
 * @author The Code Warriors
 */
public final class Tax {
    private final String stateAbbr, stateName;
    private final BigDecimal taxRate;
    
    public Tax(String stateAbbrev, String stateName, BigDecimal taxRate) {
        this.stateAbbr = stateAbbrev;
        this.stateName = stateName;
        this.taxRate = taxRate;
    }
    
    public Tax(String stateAbbrev, BigDecimal taxRate) {
        this.stateAbbr = stateAbbrev;
        this.stateName = states.get(stateAbbrev);
        this.taxRate = taxRate;
    }

    public String getStateAbbr() {
        return stateAbbr;
    }

    public String getStateName() {
        return stateName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }
    
    @Override
    public String toString() {
        return "State Abbreviation: " + this.stateAbbr + "\nState Name: " + 
                this.stateName + "\nTax Rate: " + this.taxRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        
        if (o == null || getClass() != o.getClass()) return false;
            
        Tax tax = (Tax) o;
        
        return
            this.stateAbbr.equals(tax.stateAbbr) &&
            this.stateName.equals(tax.stateName) &&
            this.taxRate.equals(tax.taxRate);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.stateAbbr);
        hash = 71 * hash + Objects.hashCode(this.stateName);
        hash = 71 * hash + Objects.hashCode(this.taxRate);
        return hash;
    }
}
