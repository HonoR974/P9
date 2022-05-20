package com.dummy.myerp.consumer.dao.impl.db.rowmapper.comptabilite;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;


/**
 * {@link RowMapper} de {@link JournalComptable}
 */
public class JournalComptableRM implements RowMapper<JournalComptable> {

    @Override
    public JournalComptable mapRow(ResultSet pRS, int pRowNum) throws SQLException {
        JournalComptable vBean = new JournalComptable();
        vBean.setCode(pRS.getString("code"));
        vBean.setLibelle(pRS.getString("libelle"));

        return vBean;
    }
}
