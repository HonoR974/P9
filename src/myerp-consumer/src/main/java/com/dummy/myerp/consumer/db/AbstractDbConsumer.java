package com.dummy.myerp.consumer.db;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import com.dummy.myerp.consumer.ConsumerHelper;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;


/**
 * <p>Classe mère des classes de Consumer DB</p>
 */
public abstract class AbstractDbConsumer {

// ==================== Attributs Static ====================
    /** Logger Log4j pour la classe */
    private static final Logger LOGGER = LogManager.getLogger(AbstractDbConsumer.class);


    /** Map des DataSources */
    private static Map<DataSourcesEnum, DataSource> mapDataSource;


    // ==================== Constructeurs ====================

    /**
     * Constructeur.
     */
    protected AbstractDbConsumer() {
        super();
    }


    // ==================== Getters/Setters ====================
    /**
     * Renvoie une {@link DaoProxy}
     *
     * @return {@link DaoProxy}
     */
    protected static DaoProxy getDaoProxy() {
        return ConsumerHelper.getDaoProxy();
    }


    // ==================== Méthodes ====================
    /**
     * Renvoie le {@link DataSource} associé demandée
     *
     * @param pDataSourceId -
     * @return SimpleJdbcTemplate
     */
    protected DataSource getDataSource(DataSourcesEnum pDataSourceId) {
        DataSource vRetour = this.mapDataSource.get(pDataSourceId);
        if (vRetour == null) {
            throw new UnsatisfiedLinkError("La DataSource suivante n'a pas été initialisée : " + pDataSourceId);
        }
        return vRetour;
    }


    /**
     * Renvoie le dernière valeur utilisé d'une séquence
     *
     * <p><i><b>Attention : </b>Méthode spécifique au SGBD PostgreSQL</i></p>
     *
     * @param <T> : La classe de la valeur de la séquence.
     * @param pDataSourcesId : L'identifiant de la {@link DataSource} à utiliser
     * @param pSeqName : Le nom de la séquence dont on veut récupérer la valeur
     * @param pSeqValueClass : Classe de la valeur de la séquence
     * @return la dernière valeur de la séquence
     */
    protected <T> T queryGetSequenceValuePostgreSQL(DataSourcesEnum pDataSourcesId,
                                                    String pSeqName, Class<T> pSeqValueClass) {

        JdbcTemplate vJdbcTemplate = new JdbcTemplate(getDataSource(pDataSourcesId));
        String vSeqSQL = "SELECT last_value FROM " + pSeqName;
        T vSeqValue = vJdbcTemplate.queryForObject(vSeqSQL, pSeqValueClass);

        return vSeqValue;
    }


    // ==================== Méthodes Static ====================
    /**
     * Méthode de configuration de la classe
     *
     * @param pMapDataSource -
     */
    public static void configure(Map<DataSourcesEnum, DataSource> pMapDataSource) {
        // On pilote l'ajout avec l'Enum et on ne rajoute pas tout à l'aveuglette...
        //   ( pas de AbstractDbDao.mapDataSource.putAll(...) )
        Map<DataSourcesEnum, DataSource> vMapDataSource = new HashMap<>(DataSourcesEnum.values().length);
        DataSourcesEnum[] vDataSourceIds = DataSourcesEnum.values();
        for (DataSourcesEnum vDataSourceId : vDataSourceIds) {
            DataSource vDataSource = pMapDataSource.get(vDataSourceId);
            // On test si la DataSource est configurée
            // (NB : elle est considérée comme configurée si elle est dans pMapDataSource mais à null)
            if (vDataSource == null) {
                if (!pMapDataSource.containsKey(vDataSourceId)) {
                    LOGGER.error("La DataSource " + vDataSourceId + " n'a pas été initialisée !");
                }
            } else {
                vMapDataSource.put(vDataSourceId, vDataSource);
            }
        }
        mapDataSource = vMapDataSource;
    }
}
