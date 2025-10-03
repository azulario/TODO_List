// JavaScript para aplicação de lista de tarefas (To-Do List)
// Todas as funções estão comentadas explicando sua finalidade

// Array para armazenar as tarefas em memória
let tasks = [];
let editIndex = null; // Índice da tarefa em edição, se houver
const storageKey = 'todo.Tasks.v1'; // Chave para armazenamento local (localStorage)

// Função para carregar tarefas do localStorage
function loadTasks() {
    const storedTasks = localStorage.getItem(storageKey); // procura dados salvos

    if (storedTasks) {
        try {
            tasks = JSON.parse(storedTasks) || [];
        } catch (e) {
            // Se houver erro de parse, zera a lista para evitar quebra
            tasks = [];
        }
    } else {
        tasks = []; //se nao tem nada salvo, começa com array vazio
    }
}

// nova função para salvar tarefas no localStorage
// salva todas as tarefas no navegador
// JSON.stringify converte o array em texto para pode salvar
// localStorage.setItem salva o texto com a chave definida
function saveTasks() {
    localStorage.setItem(storageKey, JSON.stringify(tasks)); // converte array em texto e salva
}


function renderTasks() {
  const todoList = document.getElementById('todo-list');
  const doingList = document.getElementById('doing-list');
  const doneList = document.getElementById('done-list');

  if (!todoList || !doingList || !doneList) return;

  todoList.innerHTML = '';
  doingList.innerHTML = '';
  doneList.innerHTML = '';

  tasks.forEach((task, idx) => {
    const li = document.createElement('li');
    li.className = task.status;
    li.innerHTML = `
      <input type="checkbox" class="task-checkbox" data-idx="${idx}">
      <div class="task-info">
        <strong>${task.title}</strong>
        <span>${task.description || ''}</span>
        <small>${task.dueDate || '-'}</small>
      </div>
      <div class="task-actions">
        <button onclick="editTask(${idx})" title="Editar"><span class="material-symbols-outlined" style="font-variation-settings: 'wght' 700;">edit</span></button>
        <button onclick="deleteTask(${idx})" class="delete" title="Excluir"><span class="material-symbols-outlined" style="font-variation-settings: 'wght' 700;">delete</span></button>
      </div>
    `;

    if (task.status === 'TODO') todoList.appendChild(li);
    else if (task.status === 'DOING') doingList.appendChild(li);
    else if (task.status === 'DONE') doneList.appendChild(li);
  });

  // atualiza a visibilidade dos controles de cada coluna
  updateColumnControls();
}

function updateColumnControls() {
  const statuses = ['TODO', 'DOING', 'DONE'];
  statuses.forEach((status) => {
    const listId = status === 'TODO' ? 'todo-list' : status === 'DOING' ? 'doing-list' : 'done-list';
    const list = document.getElementById(listId);
    const control = document.getElementById(`control-${status}`);
    if (!list || !control) return;

    const checked = list.querySelectorAll('.task-checkbox:checked');

    // Limpa estado visual
    list.querySelectorAll('li').forEach((li) => li.classList.remove('selected'));

    if (checked.length > 0) {
      control.classList.remove('hidden'); // mostra o controle quando há seleção
      checked.forEach((cb) => {
        const li = cb.closest('li');
        if (li) li.classList.add('selected');
      });
    } else {
      control.classList.add('hidden'); // esconde quando não há seleção
    }
  });
}

function applyColumnStatusChange(columnStatus) {
  const selectEl = document.getElementById(`status-select-${columnStatus}`);
  if (!selectEl || !selectEl.value) {
    alert('Por favor, selecione um status!');
    return;
  }

  const newStatus = selectEl.value;
  const listId = columnStatus === 'TODO' ? 'todo-list' : columnStatus === 'DOING' ? 'doing-list' : 'done-list';
  const list = document.getElementById(listId);
  if (!list) return;

  const checkedBoxes = list.querySelectorAll('.task-checkbox:checked');
  if (checkedBoxes.length === 0) {
    alert('Nenhuma tarefa selecionada!');
    return;
  }

  const confirmMsg = `Mover ${checkedBoxes.length} tarefa(s) para "${newStatus}"?`;
  if (!confirm(confirmMsg)) return;

  checkedBoxes.forEach((checkbox) => {
    const idx = parseInt(checkbox.dataset.idx, 10);
    if (!Number.isNaN(idx)) {
      tasks[idx].status = newStatus;
    }
  });

  saveTasks();
  renderTasks();

  // reseta o select da coluna
  selectEl.value = '';
}

// Função para adicionar ou atualizar uma tarefa
function handleFormSubmit(event) {
  event.preventDefault();
  const title = document.getElementById('title').value.trim();
  const description = document.getElementById('description').value.trim();
  const dueDate = document.getElementById('dueDate').value;
  const status = document.getElementById('status').value;

  if (!title) return;

  const task = { title, description, dueDate, status };

  if (editIndex !== null) {
    // Atualiza tarefa existente
    tasks[editIndex] = task;
    editIndex = null;
    document.getElementById('task-form').querySelector('button[type="submit"]').textContent = 'Adicionar';
  } else {
    // Adiciona nova tarefa
    tasks.push(task);
  }

  document.getElementById('task-form').reset();
  saveTasks(); // Salva as mudanças no localStorage
  renderTasks();
}

// Função para editar uma tarefa existente
function editTask(idx) {
  const task = tasks[idx];
  document.getElementById('title').value = task.title;
  document.getElementById('description').value = task.description;
  document.getElementById('dueDate').value = task.dueDate;
  document.getElementById('status').value = task.status;
  editIndex = idx;
  document.getElementById('task-form').querySelector('button[type="submit"]').textContent = 'Salvar';
}

// Função para excluir uma tarefa
function deleteTask(idx) {
  if (confirm('Tem certeza que deseja excluir esta tarefa?')) {
    tasks.splice(idx, 1);
    saveTasks(); // Salva as mudanças no localStorage
    renderTasks();
  }
}

// Inicialização dos listeners
window.onload = function() {
  loadTasks(); // Carrega tarefas do localStorage ao iniciar

  const form = document.getElementById('task-form');
  if (form) {
    form.addEventListener('submit', handleFormSubmit);
  }

  // Delegação de evento para mudanças nos checkboxes das tarefas
  const container = document.querySelector('.container');
  if (container) {
    container.addEventListener('change', function(event) {
      if (event.target && event.target.classList && event.target.classList.contains('task-checkbox')) {
        updateColumnControls();
      }
    });
  }

  renderTasks();
};
