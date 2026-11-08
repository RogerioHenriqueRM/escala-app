
package com.escalarelatorio.app.database

class ConfiguracaoEmpresaRepository(
    private val dao: EscalaDao
) {

    fun observar() = dao.observarConfiguracao()

    suspend fun carregar(): ConfiguracaoEmpresaEntity {
        val existente = dao.buscarConfiguracao()

        if (existente != null) {
            return existente
        }

        val padrao = ConfiguracaoEmpresaEntity()

        dao.salvarConfiguracao(padrao)

        return padrao
    }

    suspend fun salvar(
        configuracao: ConfiguracaoEmpresaEntity
    ) {
        dao.salvarConfiguracao(
            configuracao.copy(id = 1)
        )
    }
}

