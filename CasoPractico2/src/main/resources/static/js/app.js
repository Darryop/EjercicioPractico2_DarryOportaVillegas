// Funciones generales para la plataforma
document.addEventListener('DOMContentLoaded', function() {
    
    // Confirmación para eliminar
    const deleteButtons = document.querySelectorAll('.btn-delete-confirm');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm('¿Está seguro de que desea eliminar este registro?')) {
                e.preventDefault();
            }
        });
    });
    
    // Validación de formularios
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const requiredFields = form.querySelectorAll('[required]');
            let isValid = true;
            
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    isValid = false;
                    field.classList.add('is-invalid');
                    
                    // Crear mensaje de error si no existe
                    if (!field.nextElementSibling || !field.nextElementSibling.classList.contains('invalid-feedback')) {
                        const errorDiv = document.createElement('div');
                        errorDiv.className = 'invalid-feedback';
                        errorDiv.textContent = 'Este campo es requerido';
                        field.parentNode.appendChild(errorDiv);
                    }
                } else {
                    field.classList.remove('is-invalid');
                    field.classList.add('is-valid');
                }
            });
            
            if (!isValid) {
                e.preventDefault();
                showToast('Por favor, complete todos los campos requeridos', 'error');
            }
        });
    });
    
    // Mostrar/ocultar contraseña
    const togglePasswordButtons = document.querySelectorAll('.toggle-password');
    togglePasswordButtons.forEach(button => {
        button.addEventListener('click', function() {
            const input = document.querySelector(this.getAttribute('data-target'));
            const icon = this.querySelector('i');
            
            if (input.type === 'password') {
                input.type = 'text';
                icon.classList.remove('bi-eye');
                icon.classList.add('bi-eye-slash');
            } else {
                input.type = 'password';
                icon.classList.remove('bi-eye-slash');
                icon.classList.add('bi-eye');
            }
        });
    });
    
    // Auto-ocultar mensajes de alerta
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => {
                alert.style.display = 'none';
            }, 300);
        }, 5000);
    });
    
    // Filtro en tablas
    const tableFilters = document.querySelectorAll('.table-filter');
    tableFilters.forEach(filter => {
        filter.addEventListener('keyup', function() {
            const value = this.value.toLowerCase();
            const tableId = this.getAttribute('data-table');
            const table = document.getElementById(tableId);
            const rows = table.querySelectorAll('tbody tr');
            
            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(value) ? '' : 'none';
            });
        });
    });
    
    // Contador de caracteres
    const charCounters = document.querySelectorAll('.char-counter');
    charCounters.forEach(counter => {
        const input = document.getElementById(counter.getAttribute('data-input'));
        const max = parseInt(counter.getAttribute('data-max')) || 255;
        
        input.addEventListener('input', function() {
            const current = this.value.length;
            counter.textContent = `${current}/${max}`;
            
            if (current > max) {
                counter.classList.add('text-danger');
            } else {
                counter.classList.remove('text-danger');
            }
        });
        
        // Inicializar contador
        counter.textContent = `${input.value.length}/${max}`;
    });
});

// Función para mostrar notificaciones tipo toast
function showToast(message, type = 'info') {
    // Crear contenedor si no existe
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 9999;
            display: flex;
            flex-direction: column;
            gap: 10px;
        `;
        document.body.appendChild(container);
    }
    
    // Crear toast
    const toast = document.createElement('div');
    toast.className = `toast bg-${type} text-white`;
    toast.style.cssText = `
        padding: 15px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        animation: slideIn 0.3s ease-out;
        display: flex;
        align-items: center;
        justify-content: space-between;
        min-width: 300px;
    `;
    
    // Contenido del toast
    toast.innerHTML = `
        <div style="flex: 1;">
            <i class="bi ${getIconForType(type)} me-2"></i>
            ${message}
        </div>
        <button type="button" class="btn-close btn-close-white" 
                onclick="this.parentElement.remove()" 
                style="margin-left: 15px;">
        </button>
    `;
    
    // Agregar toast al contenedor
    container.appendChild(toast);
    
    // Auto-eliminar después de 5 segundos
    setTimeout(() => {
        if (toast.parentElement) {
            toast.style.animation = 'slideOut 0.3s ease-out';
            setTimeout(() => {
                if (toast.parentElement) {
                    toast.remove();
                }
            }, 300);
        }
    }, 5000);
}

// Obtener icono según tipo
function getIconForType(type) {
    switch(type) {
        case 'success': return 'bi-check-circle-fill';
        case 'error': return 'bi-exclamation-circle-fill';
        case 'warning': return 'bi-exclamation-triangle-fill';
        default: return 'bi-info-circle-fill';
    }
}

// Agregar estilos de animación
const style = document.createElement('style');
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(100%);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOut {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(100%);
            opacity: 0;
        }
    }
    
    .is-valid {
        border-color: #198754 !important;
        background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 8 8'%3e%3cpath fill='%23198754' d='M2.3 6.73L.6 4.53c-.4-1.04.46-1.4 1.1-.8l1.1 1.4 3.4-3.8c.6-.63 1.6-.27 1.2.7l-4 4.6c-.43.5-.8.4-1.1.1z'/%3e%3c/svg%3e") !important;
    }
    
    .is-invalid {
        border-color: #dc3545 !important;
        background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 12 12' width='12' height='12' fill='none' stroke='%23dc3545'%3e%3ccircle cx='6' cy='6' r='4.5'/%3e%3cpath stroke-linejoin='round' d='M5.8 3.6h.4L6 6.5z'/%3e%3ccircle cx='6' cy='8.2' r='.6' fill='%23dc3545' stroke='none'/%3e%3c/svg%3e") !important;
    }
    
    .invalid-feedback {
        display: block;
        color: #dc3545;
        font-size: 0.875rem;
        margin-top: 0.25rem;
    }
`;
document.head.appendChild(style);

// Función para exportar tabla a CSV
function exportTableToCSV(tableId, filename) {
    const table = document.getElementById(tableId);
    const rows = table.querySelectorAll('tr');
    const csv = [];
    
    rows.forEach(row => {
        const cols = row.querySelectorAll('td, th');
        const rowData = [];
        
        cols.forEach(col => {
            // Remover botones y elementos no deseados
            const clone = col.cloneNode(true);
            const buttons = clone.querySelectorAll('button, a');
            buttons.forEach(btn => btn.remove());
            rowData.push(clone.textContent.trim());
        });
        
        csv.push(rowData.join(','));
    });
    
    const csvString = csv.join('\n');
    const blob = new Blob([csvString], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    
    a.setAttribute('hidden', '');
    a.setAttribute('href', url);
    a.setAttribute('download', filename || 'export.csv');
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
}

// Función para volver arriba
function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

// Contador de usuarios en línea (simulado)
function updateOnlineUsers() {
    const onlineBadge = document.querySelector('.footer .badge.bg-success');
    if (onlineBadge) {
        // Simular cambio en usuarios en línea
        const baseUsers = 12;
        const randomChange = Math.floor(Math.random() * 5) - 2; // -2 a +2
        const newCount = Math.max(5, baseUsers + randomChange);
        onlineBadge.textContent = newCount;
    }
}

// Actualizar cada 30 segundos
setInterval(updateOnlineUsers, 30000);

// Inicializar cuando se carga la página
document.addEventListener('DOMContentLoaded', function() {
    // Agregar año actual al copyright
    const yearSpan = document.querySelector('[data-current-year]');
    if (yearSpan) {
        yearSpan.textContent = new Date().getFullYear();
    }
    
    // Mostrar año en el copyright si no está
    const copyrightText = document.querySelector('.footer p:first-child');
    if (copyrightText && !copyrightText.textContent.includes('2024')) {
        copyrightText.innerHTML = copyrightText.innerHTML.replace('2024', new Date().getFullYear());
    }
});