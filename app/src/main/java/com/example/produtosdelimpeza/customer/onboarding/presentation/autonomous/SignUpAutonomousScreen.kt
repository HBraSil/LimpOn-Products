package com.example.produtosdelimpeza.customer.onboarding.presentation.autonomous

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.produtosdelimpeza.R

data class CadastroUiState(
    val fotoUri: Uri? = null,
    val nomeCompleto: String = "",
    val profissoesSelecionadas: List<String> = emptyList(),
    val profissaoDropdownExpandido: Boolean = false,
    val profissaoSelecionadaNoDropdown: String = "",
    val outraProfissaoTexto: String = "",
    val servicoInput: String = "",
    val servicosAdicionados: List<String> = emptyList(),
    val precoDiaria: String = "",
    val precoHora: String = "",
    val horarioAtendimento: String = "Seg - Sex, 08:00 - 18:00",
    val localizacao: String = "São Paulo, SP e arredores",
    val midias: List<Uri> = emptyList()
)


@Composable
fun CadastroProfissionalScreen(
    state: CadastroUiState,
    onNomeChange: (String) -> Unit,
    onDropdownExpandedChange: (Boolean) -> Unit,
    onProfissaoSelected: (String) -> Unit,
    onOutraProfissaoChange: (String) -> Unit,
    onRemoverProfissao: (String) -> Unit,
    onServicoInputChange: (String) -> Unit,
    onAdicionarServico: () -> Unit,
    onRemoverServico: (String) -> Unit,
    onPrecoDiariaChange: (String) -> Unit,
    onPrecoHoraChange: (String) -> Unit,
    onFinalizarCadastro: () -> Unit,
) {
    CadastroProfissionalContent(
        state = state,
        onDropdownExpandedChange = onDropdownExpandedChange,
        onNomeChange = onNomeChange,
        onServicoInputChange = onServicoInputChange,
        onAdicionarServico = onAdicionarServico,
        onPrecoDiariaChange = onPrecoDiariaChange,
        onPrecoHoraChange = onPrecoHoraChange,
        onFinalizarCadastro = onFinalizarCadastro,
        onProfissaoSelected = onProfissaoSelected,
        onOutraProfissaoChange = onOutraProfissaoChange,
        onRemoverProfissao = onRemoverProfissao,
        onRemoverServico = onRemoverServico,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroProfissionalContent(
    state: CadastroUiState,
    onDropdownExpandedChange: (Boolean) -> Unit,
    onNomeChange: (String) -> Unit,
    onServicoInputChange: (String) -> Unit,
    onAdicionarServico: () -> Unit,
    onPrecoDiariaChange: (String) -> Unit,
    onPrecoHoraChange: (String) -> Unit,
    onFinalizarCadastro: () -> Unit,
    onProfissaoSelected: (String) -> Unit,
    onOutraProfissaoChange: (String) -> Unit,
    onRemoverProfissao: (String) -> Unit,
    onRemoverServico: (String) -> Unit
) {
    val profissoesPredefinidas = listOf("Eletricista", "Encanador", "Pintor", "Pedreiro", "Outro")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cadastro Profissional") },
                navigationIcon = {
                    IconButton(onClick = { /* Voltar */ }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ){
            LazyColumn(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                item {
                    Text(
                        text = "Seja bem-vindo ao Marketplace",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Complete seu perfil para começar a oferecer seus serviços e atrair novos clientes.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                // Foto de Perfil
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(contentAlignment = Alignment.BottomEnd) {
                                Box(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF3E5F5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.AddAPhoto,
                                        contentDescription = null,
                                        tint = Color(0xFF512DA8)
                                    )
                                }
                                IconButton(
                                    onClick = { /* Tirar/Escolher foto */ },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color(0xFF512DA8), CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Foto de Perfil",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Nome Completo
                item {
                    OutlinedTextField(
                        value = state.nomeCompleto,
                        onValueChange = onNomeChange,
                        label = { Text("Nome Completo") },
                        placeholder = { Text("Ex: Roberto Silva") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                // SEÇÃO 1: Profissão(ões) com ExposedDropdownMenu e Condicional "Outro"
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Profissão(ões)", fontWeight = FontWeight.SemiBold)

                        // ExposedDropdownMenu
                        ExposedDropdownMenuBox(
                            expanded = state.profissaoDropdownExpandido,
                            onExpandedChange = {
                                onDropdownExpandedChange(!state.profissaoDropdownExpandido)
                            } // Mantém o hoisting do container
                        ) {
                            OutlinedTextField(
                                value = state.profissaoSelecionadaNoDropdown,
                                onValueChange = {},
                                readOnly = true, // Mantém true para não abrir o teclado do sistema
                                label = { Text("Selecionar Profissão") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.profissaoDropdownExpandido)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor() // Garante o posicionamento correto do menu
                                    .clickable {
                                        // CORREÇÃO AQUI: Força o clique a alternar o estado do dropdown
                                        onDropdownExpandedChange(!state.profissaoDropdownExpandido)
                                    },
                                shape = RoundedCornerShape(12.dp)
                            )

                            ExposedDropdownMenu(
                                expanded = state.profissaoDropdownExpandido,
                                onDismissRequest = { onDropdownExpandedChange(false) }
                            ) {
                                profissoesPredefinidas.forEach { profissao ->
                                    DropdownMenuItem(
                                        text = { Text(profissao) },
                                        onClick = {
                                            onProfissaoSelected(profissao)
                                            onDropdownExpandedChange(false) // Fecha após selecionar
                                        }
                                    )
                                }
                            }
                        }


                        // Condicional do campo "Digite sua profissão" (Só aparece se "Outro" for selecionado)
                        AnimatedVisibility(visible = state.profissaoSelecionadaNoDropdown == "Outro") {
                            OutlinedTextField(
                                value = state.outraProfissaoTexto,
                                onValueChange = onOutraProfissaoChange,
                                label = { Text("Digite sua profissão") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        if (state.outraProfissaoTexto.isNotBlank()) {
                                            onProfissaoSelected(state.outraProfissaoTexto)
                                        }
                                    }) {
                                        Icon(
                                            Icons.Default.Check,
                                            contentDescription = "Adicionar outra"
                                        )
                                    }
                                }
                            )
                        }
                    }


                    // Chips de profissões já selecionadas
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.profissoesSelecionadas.forEach { profissao ->
                            InputChip(
                                selected = true,
                                onClick = { onRemoverProfissao(profissao) },
                                label = { Text(profissao, color = Color.White) },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remover",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                },
                                colors = InputChipDefaults.inputChipColors(
                                    containerColor = Color(
                                        0xFF673AB7
                                    )
                                )
                            )
                        }
                    }
                }

                // SEÇÃO 2: Serviços Oferecidos (A lista só popula ao clicar em Adicionar)
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Serviços Oferecidos", fontWeight = FontWeight.SemiBold)

                        OutlinedTextField(
                            value = state.servicoInput,
                            onValueChange = onServicoInputChange,
                            placeholder = { Text("Ex: Instalação de luminárias") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = {
                                TextButton(onClick = onAdicionarServico) {
                                    Text(
                                        "Adicionar",
                                        color = Color(0xFF512DA8),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Lista dinâmica dos serviços adicionados pelo usuário
                        state.servicosAdicionados.forEach { servico ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF512DA8),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = servico,
                                    modifier = Modifier.weight(1.0f),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                IconButton(onClick = { onRemoverServico(servico) }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Remover serviço",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }

                // Precificação
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Precificação", fontWeight = FontWeight.SemiBold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedTextField(
                                value = state.precoDiaria,
                                onValueChange = onPrecoDiariaChange,
                                label = { Text("Preço da Diária") },
                                prefix = { Text("R$ ") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            OutlinedTextField(
                                value = state.precoHora,
                                onValueChange = onPrecoHoraChange,
                                label = { Text("Preço da Hora") },
                                prefix = { Text("R$ ") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                    }
                }

                // Horário e Localização (Estilo List Item clicável)
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ConfigRow(
                            icon = Icons.Default.AccessTime,
                            title = "Horário de Atendimento",
                            subtitle = state.horarioAtendimento
                        )
                        ConfigRow(
                            icon = Icons.Default.LocationOn,
                            title = "Localização",
                            subtitle = state.localizacao
                        )
                    }
                }

                // Meus Serviços (Mídias/Fotos do portfólio)
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Meus Serviços", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            TextButton(onClick = { /* Ver todos */ }) {
                                Text("Ver todos", color = Color(0xFF512DA8))
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Card de adicionar mídia (Dashed border simulado com formato)
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF3E5F5))
                                    .clickable { /* Adicionar mídia */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Default.AddPhotoAlternate,
                                        contentDescription = null,
                                        tint = Color(0xFF512DA8)
                                    )
                                    Text("Adicionar", fontSize = 12.sp, color = Color(0xFF512DA8))
                                }
                            }

                            // Placeholders para as imagens que já existem na sua UI de exemplo
                            repeat(2) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.LightGray) // Aqui entraria o AsyncImage da Coil
                                )
                            }
                        }
                    }
                }

                // Espaçamento extra para o botão do bottomBar não cobrir o conteúdo
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }

            Button(
                onClick = onFinalizarCadastro,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
                    .align(Alignment.BottomCenter),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF512DA8)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Finalizar Cadastro", fontSize = 18.sp, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}

// Componente auxiliar para as linhas de Horário e Localização
@Composable
fun ConfigRow(icon: ImageVector, title: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF512DA8))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(subtitle, color = Color.Gray, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfessionSection(
    selectedProfessions: List<String>,
    professions: List<String>,
    showCustomProfessionField: Boolean,
    customProfession: String,
    onProfessionSelected: (String) -> Unit,
    onProfessionRemoved: (String) -> Unit,
    onCustomProfessionChange: (String) -> Unit
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    Column {

        Text("Profissão(ões)")

        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {

            OutlinedTextField(
                value = "",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(
                        MenuAnchorType.PrimaryNotEditable
                    ),
                placeholder = {
                    Text("Selecionar...")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults
                        .TrailingIcon(expanded)
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                professions.forEach { profession ->

                    DropdownMenuItem(
                        text = {
                            Text(profession)
                        },
                        onClick = {

                            onProfessionSelected(
                                profession
                            )

                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        ProfessionChips(
            professions = selectedProfessions,
            onRemoveProfession =
                onProfessionRemoved
        )

        AnimatedVisibility(
            visible = showCustomProfessionField
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                value = customProfession,
                onValueChange =
                    onCustomProfessionChange,
                placeholder = {
                    Text("Digite sua profissão")
                }
            )
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ProfessionChips(
    professions: List<String>,
    onRemoveProfession: (String) -> Unit
) {

    FlowRow(
        horizontalArrangement =
            Arrangement.spacedBy(8.dp),
        verticalArrangement =
            Arrangement.spacedBy(8.dp)
    ) {

        professions.forEach { profession ->

            InputChip(
                selected = true,
                onClick = {},
                label = {
                    Text(profession)
                },
                trailingIcon = {

                    Icon(
                        modifier = Modifier
                            .size(18.dp)
                            .clickable {
                                onRemoveProfession(
                                    profession
                                )
                            },
                        imageVector =
                            Icons.Default.Close,
                        contentDescription = null
                    )
                }
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CadastroProfissionalScreenPreview() {
    // Fornecemos um estado mockado (de exemplo) para visualizar como a tela fica preenchida
    var uiState by remember { mutableStateOf(CadastroUiState(
        nomeCompleto = "Roberto Silva",
        profissaoDropdownExpandido = false,
        profissoesSelecionadas = listOf("Eletricista", "Encanador"),
        profissaoSelecionadaNoDropdown = "Outro", // Força o campo "Digite sua profissão" a aparecer no Preview
        outraProfissaoTexto = "Técnico de Ar Condicionado",
        servicoInput = "Instalação de tomadas",
        servicosAdicionados = listOf(
            "Instalações elétricas completas",
            "Manutenção e reforma de quadros de luz",
            "Troca de fiação e aterramento"
        ),
        precoDiaria = "400,00",
        precoHora = "60,00"
    )) }

    // Passamos lambdas vazias {} para os eventos, já que o preview é apenas visual
    CadastroProfissionalScreen(
        state = uiState,
        onNomeChange = {},
        onDropdownExpandedChange = {
            uiState = uiState.copy(profissaoDropdownExpandido = it)
        },
        onProfissaoSelected = {
            uiState = uiState.copy(profissaoSelecionadaNoDropdown = it)
        },
        onOutraProfissaoChange = {},
        onRemoverProfissao = {},
        onServicoInputChange = {},
        onAdicionarServico = {},
        onRemoverServico = {},
        onPrecoDiariaChange = {},
        onPrecoHoraChange = {},
        onFinalizarCadastro = {}
    )
}