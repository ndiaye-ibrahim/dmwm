package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CelluleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cellule.class);
        Cellule cellule1 = new Cellule();
        cellule1.setId(1L);
        Cellule cellule2 = new Cellule();
        cellule2.setId(cellule1.getId());
        assertThat(cellule1).isEqualTo(cellule2);
        cellule2.setId(2L);
        assertThat(cellule1).isNotEqualTo(cellule2);
        cellule1.setId(null);
        assertThat(cellule1).isNotEqualTo(cellule2);
    }
}
