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
        tasks = JSON.parse(storedTasks); // converte de volta para array
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
        <small> ${task.dueDate || '-'}</small>
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
    const columns = ['TODO', 'DOING', 'DONE'];
    // pega todos os checkboxes marcados nessa coluna especifica
    columns.forEach(status => {
        const listId = column === 'TODO' ? 'todo-list' :
            column === 'DOING' ? 'doing-list' : 'done-list';

        const list = document.getElementById(listId);
        const checkInColumn = list.querySelectorAll('.task-checkbox:checked');

        // pega o controle dessa coluna
        const control = document.getElementById(`control-${column}`);

        if (checkInColumn.length > 0) {
            control.classList.add('hidden');

            checkInColumn.forEach(check => {
                check.closest('li').classList.add('selected');
            });
        } else {
            control.classList.remove('hidden');

            const allInColumn = list.querySelectorAll('li');
            allInColumn.forEach(li => li.classList.remove('selected'));
        }
    });
}

function applyColumnStatusChange(columnStatus) {
    const newStatus = document.getElementById(`status-select-${columnStatus}`);

    if (!newStatus) {
        alert('Por favor, selecione um status!');
        return;
    }

    const listId = columnStatus === 'TODO' ? 'todo-list' :
        columnStatus === 'DOING' ? 'doing-list' : 'done-list';
    const list = document.getElementById(listId);

    const checkedBoxes = list.querySelectorAll('.task-checkbox:checked');

    if (checkedBoxes.length === 0) {
        alert('Nenhuma tarefa selecionada!');
        return;
    }

    const statusLabel = getStatusLabel(newStatus);
    const confirmMsg = `Mover ${checkBoxes.length} tarefa(s) para "${statusLabel}"?)`;
    if (!confirm(confirmMsg)) return;

    checkedBoxes.forEach(checkbox => {
        const idx = parseInt(checkbox.dataset.idx);
        tasks[idx].status = newStatus;
    });

    saveTasks();
    renderTasks();

    document.getElementById(`status-select-${columnStatus}`).value = '';

    alert('Tarefas movidas com sucesso!');

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

  document.getElementById('task-form').addEventListener('submit', handleFormSubmit);
  renderTasks();

  document.querySelector('.container').addEventListener('chage', function(event) {
      if (event.target.classList.contains('task-checkbox')) {
          updateColumnControls();
      }
  });
  renderTasks();
};
