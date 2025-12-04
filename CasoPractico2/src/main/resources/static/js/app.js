/**
 * JavaScript personalizado para la Plataforma Académica
 */

document.addEventListener('DOMContentLoaded', function() {
    
    // ===========================================
    // CONFIRMACIÓN PARA ELIMINACIONES
    // ===========================================
    const deleteButtons = document.querySelectorAll('.btn-delete, a[href*="eliminar"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm('¿Estás seguro de que deseas eliminar este registro? Esta acción no se puede deshacer.')) {
                e.preventDefault();
                e.stopPropagation();
            }
        });
    });
    
    // ===========================================
    // AUTO-OCULTAR ALERTAS DESPUÉS DE 5 SEGUNDOS
    // ===========================================
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
        alerts.forEach(alert => {
            const closeButton = alert.querySelector('.btn-close');
            if (closeButton) {
                closeButton.click();
            } else {
                alert.style.opacity = '0';
                alert.style.transition = 'opacity 0.5s ease';
                setTimeout(() => alert.remove(), 500);
            }
        });
    }, 5000);
    
    // ===========================================
    // FORMATEAR FECHAS
    // ===========================================
    const fechaElements = document.querySelectorAll('.fecha-formateada');
    fechaElements.forEach(element => {
        const fechaTexto = element.textContent.trim();
        if (fechaTexto) {
            try {
                const fecha = new Date(fechaTexto);
                if (!isNaN(fecha)) {
                    element.textContent = fecha.toLocaleDateString('es-ES', {
                        year: 'numeric',
                        month: 'long',
                        day: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                    });
                }
            } catch (error) {
                console.log('Error formateando fecha:', error);
            }
        }
    });
    
    // ===========================================
    // VALIDACIÓN DE FORMULARIOS
    // ===========================================
    const forms = document.querySelectorAll('.needs-validation');
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
                
                // Resaltar campos inválidos
                const invalidFields = form.querySelectorAll(':invalid');
                invalidFields.forEach(field => {
                    field.classList.add('is-invalid');
                    
                    // Crear mensaje de error si no existe
                    if (!field.nextElementSibling || !field.nextElementSibling.classList.contains('invalid-feedback')) {
                        const errorDiv = document.createElement('div');
                        errorDiv.className = 'invalid-feedback';
                        errorDiv.textContent = field.validationMessage || 'Este campo es obligatorio';
                        field.parentNode.insertBefore(errorDiv, field.nextSibling);
                    }
                });
            }
            form.classList.add('was-validated');
        }, false);
        
        // Limpiar validación cuando el usuario escribe
        form.querySelectorAll('input, select, textarea').forEach(field => {
            field.addEventListener('input', function() {
                if (this.classList.contains('is-invalid')) {
                    this.classList.remove('is-invalid');
                    const errorDiv = this.nextElementSibling;
                    if (errorDiv && errorDiv.classList.contains('invalid-feedback')) {
                        errorDiv.remove();
                    }
                }
            });
        });
    });
    
    // ===========================================
    // TOOLTIPS
    // ===========================================
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // ===========================================
    // POPOVERS
    // ===========================================
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
    
    // ===========================================
    // TOGGLE PASSWORD VISIBILITY
    // ===========================================
    const togglePasswordButtons = document.querySelectorAll('.toggle-password');
    togglePasswordButtons.forEach(button => {
        button.addEventListener('click', function() {
            const passwordInput = this.previousElementSibling;
            const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
            passwordInput.setAttribute('type', type);
            
            // Cambiar ícono
            const icon = this.querySelector('i');
            if (type === 'text') {
                icon.classList.remove('fa-eye');
                icon.classList.add('fa-eye-slash');
                this.setAttribute('aria-label', 'Ocultar contraseña');
            } else {
                icon.classList.remove('fa-eye-slash');
                icon.classList.add('fa-eye');
                this.setAttribute('aria-label', 'Mostrar contraseña');
            }
        });
    });
    
    // ===========================================
    // CONTADOR DE CARACTERES PARA TEXTAREAS
    // ===========================================
    const textareas = document.querySelectorAll('textarea[maxlength]');
    textareas.forEach(textarea => {
        const maxLength = textarea.getAttribute('maxlength');
        const counterId = textarea.id + '-counter';
        
        // Crear contador
        const counter = document.createElement('small');
        counter.id = counterId;
        counter.className = 'form-text text-muted float-end';
        counter.textContent = '0/' + maxLength;
        
        textarea.parentNode.appendChild(counter);
        
        // Actualizar contador
        textarea.addEventListener('input', function() {
            const currentLength = this.value.length;
            counter.textContent = currentLength + '/' + maxLength;
            
            if (currentLength >= maxLength) {
                counter.classList.remove('text-muted');
                counter.classList.add('text-danger');
            } else {
                counter.classList.remove('text-danger');
                counter.classList.add('text-muted');
            }
        });
        
        // Actualizar al cargar (para editores)
        textarea.dispatchEvent(new Event('input'));
    });
    
    // ===========================================
    // SIDEBAR TOGGLE PARA MÓVIL
    // ===========================================
    const sidebarToggle = document.querySelector('.sidebar-toggle');
    const sidebar = document.querySelector('.sidebar');
    
    if (sidebarToggle && sidebar) {
        sidebarToggle.addEventListener('click', function() {
            sidebar.classList.toggle('show');
            this.classList.toggle('active');
        });
    }
    
    // ===========================================
    // BUSQUEDA EN TABLAS
    // ===========================================
    const searchInputs = document.querySelectorAll('.table-search');
    searchInputs.forEach(input => {
        input.addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            const tableId = this.getAttribute('data-table');
            const table = document.getElementById(tableId);
            
            if (table) {
                const rows = table.querySelectorAll('tbody tr');
                rows.forEach(row => {
                    const text = row.textContent.toLowerCase();
                    if (text.includes(searchTerm)) {
                        row.style.display = '';
                    } else {
                        row.style.display = 'none';
                    }
                });
            }
        });
    });
    
    // ===========================================
    // ORDENACIÓN DE TABLAS
    // ===========================================
    const sortableHeaders = document.querySelectorAll('th.sortable');
    sortableHeaders.forEach(header => {
        header.style.cursor = 'pointer';
        header.addEventListener('click', function() {
            const table = this.closest('table');
            const columnIndex = Array.from(this.parentNode.children).indexOf(this);
            const isAscending = !this.classList.contains('asc');
            
            // Limpiar otras columnas
            table.querySelectorAll('th.sortable').forEach(th => {
                th.classList.remove('asc', 'desc');
            });
            
            // Establecer dirección
            this.classList.toggle('asc', isAscending);
            this.classList.toggle('desc', !isAscending);
            
            // Ordenar filas
            const tbody = table.querySelector('tbody');
            const rows = Array.from(tbody.querySelectorAll('tr'));
            
            rows.sort((a, b) => {
                const aValue = a.children[columnIndex].textContent.trim();
                const bValue = b.children[columnIndex].textContent.trim();
                
                // Intentar convertir a número si es posible
                const aNum = parseFloat(aValue);
                const bNum = parseFloat(bValue);
                
                let comparison = 0;
                if (!isNaN(aNum) && !isNaN(bNum)) {
                    comparison = aNum - bNum;
                } else {
                    comparison = aValue.localeCompare(bValue, 'es', { sensitivity: 'base' });
                }
                
                return isAscending ? comparison : -comparison;
            });
            
            // Reinsertar filas ordenadas
            rows.forEach(row => tbody.appendChild(row));
        });
    });
    
    // ===========================================
    // EXPORTACIÓN DE DATOS (SIMULADA)
    // ===========================================
    const exportButtons = document.querySelectorAll('.btn-export');
    exportButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            const format = this.getAttribute('data-format') || 'csv';
            const tableId = this.getAttribute('data-table');
            const table = document.getElementById(tableId);
            
            if (table) {
                alert(`Exportando tabla en formato ${format.toUpperCase()}...\n(Esta es una simulación. En producción, se implementaría la lógica real de exportación.)`);
                
                // Aquí iría la lógica real de exportación
                // Por ejemplo, para CSV:
                if (format === 'csv') {
                    let csv = [];
                    const rows = table.querySelectorAll('tr');
                    
                    rows.forEach(row => {
                        const rowData = [];
                        row.querySelectorAll('th, td').forEach(cell => {
                            // Excluir celdas de acción
                            if (!cell.querySelector('.btn')) {
                                rowData.push(`"${cell.textContent.trim()}"`);
                            }
                        });
                        csv.push(rowData.join(','));
                    });
                    
                    // Crear y descargar archivo
                    const blob = new Blob([csv.join('\n')], { type: 'text/csv' });
                    const url = window.URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.href = url;
                    a.download = `exportacion_${new Date().toISOString().split('T')[0]}.csv`;
                    document.body.appendChild(a);
                    a.click();
                    document.body.removeChild(a);
                    window.URL.revokeObjectURL(url);
                }
            }
        });
    });
    
    // ===========================================
    // CARGA DE IMÁGENES DE PERFIL
    // ===========================================
    const imageUploads = document.querySelectorAll('.image-upload');
    imageUploads.forEach(upload => {
        upload.addEventListener('change', function() {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                const preview = document.getElementById(this.getAttribute('data-preview'));
                
                reader.onload = function(e) {
                    if (preview) {
                        preview.src = e.target.result;
                        preview.style.display = 'block';
                    }
                };
                
                reader.readAsDataURL(file);
            }
        });
    });
    
    // ===========================================
    // INICIALIZACIÓN DE SELECT2 (SI ESTÁ DISPONIBLE)
    // ===========================================
    if (typeof $ !== 'undefined' && $.fn.select2) {
        $('.select2').select2({
            theme: 'bootstrap-5',
            placeholder: 'Seleccione una opción',
            allowClear: true
        });
    }
    
    // ===========================================
    // MENSAJES DE CONFIRMACIÓN PARA ACTUALIZACIONES
    // ===========================================
    const saveButtons = document.querySelectorAll('button[type="submit"]:not(.no-confirm)');
    saveButtons.forEach(button => {
        button.addEventListener('click', function() {
            const form = this.closest('form');
            if (form && form.classList.contains('was-validated')) {
                // Mostrar spinner de carga
                const originalText = this.innerHTML;
                this.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Guardando...';
                this.disabled = true;
                
                // Restaurar después de 2 segundos (simulación)
                setTimeout(() => {
                    this.innerHTML = originalText;
                    this.disabled = false;
                }, 2000);
            }
        });
    });
    
    // ===========================================
    // ACTUALIZACIÓN AUTOMÁTICA DE ESTADÍSTICAS
    // ===========================================
    if (window.location.pathname.includes('dashboard')) {
        setInterval(() => {
            // Simular actualización de estadísticas
            const statsElements = document.querySelectorAll('.stat-value');
            statsElements.forEach(element => {
                const currentValue = parseInt(element.textContent);
                if (!isNaN(currentValue)) {
                    // Incremento aleatorio pequeño para simular cambios
                    const change = Math.floor(Math.random() * 3) - 1; // -1, 0, o 1
                    const newValue = Math.max(0, currentValue + change);
                    element.textContent = newValue;
                    
                    // Animación
                    element.style.transform = 'scale(1.1)';
                    setTimeout(() => {
                        element.style.transform = 'scale(1)';
                    }, 300);
                }
            });
        }, 10000); // Cada 10 segundos
    }
    
    // ===========================================
    // DETECCIÓN DE CONEXIÓN
    // ===========================================
    window.addEventListener('online', function() {
        showNotification('success', 'Conexión restablecida', 'Ya estás conectado a internet.');
    });
    
    window.addEventListener('offline', function() {
        showNotification('warning', 'Conexión perdida', 'Estás trabajando sin conexión. Los cambios se guardarán localmente.');
    });
    
    // ===========================================
    // NOTIFICACIONES PERSONALIZADAS
    // ===========================================
    window.showNotification = function(type, title, message) {
        // Crear contenedor de notificaciones si no existe
        let notificationContainer = document.getElementById('notification-container');
        if (!notificationContainer) {
            notificationContainer = document.createElement('div');
            notificationContainer.id = 'notification-container';
            notificationContainer.style.cssText = 'position: fixed; top: 20px; right: 20px; z-index: 9999; width: 300px;';
            document.body.appendChild(notificationContainer);
        }
        
        // Crear notificación
        const notification = document.createElement('div');
        notification.className = `alert alert-${type} alert-dismissible fade show`;
        notification.innerHTML = `
            <strong>${title}</strong>
            <div>${message}</div>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        notificationContainer.appendChild(notification);
        
        // Auto-eliminar después de 5 segundos
        setTimeout(() => {
            if (notification.parentNode) {
                notification.remove();
            }
        }, 5000);
    };
    
    console.log('Plataforma Académica - JavaScript cargado correctamente');
});