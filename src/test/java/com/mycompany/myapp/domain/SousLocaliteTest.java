package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SousLocaliteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SousLocalite.class);
        SousLocalite sousLocalite1 = new SousLocalite();
        sousLocalite1.setId(1L);
        SousLocalite sousLocalite2 = new SousLocalite();
        sousLocalite2.setId(sousLocalite1.getId());
        assertThat(sousLocalite1).isEqualTo(sousLocalite2);
        sousLocalite2.setId(2L);
        assertThat(sousLocalite1).isNotEqualTo(sousLocalite2);
        sousLocalite1.setId(null);
        assertThat(sousLocalite1).isNotEqualTo(sousLocalite2);
    }
}
