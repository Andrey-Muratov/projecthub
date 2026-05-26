

(function () {
    'use strict';

    var pending = null;
    var pendingTimer = null;

    function isTyping(target) {
        if (!target) return false;
        var tag = (target.tagName || '').toLowerCase();
        if (tag === 'input' || tag === 'textarea' || tag === 'select') return true;
        if (target.isContentEditable) return true;
        return false;
    }

    function clearPending() {
        pending = null;
        if (pendingTimer) {
            clearTimeout(pendingTimer);
            pendingTimer = null;
        }
    }

    function navigate(url) {
        window.location.href = url;
    }

    function focusSearch() {
        var input = document.querySelector('.navbar-search input[name="q"]');
        if (input) {
            input.focus();
            input.select();
        }
    }

    function openCheatsheet() {
        var modalEl = document.getElementById('hotkeysCheatsheet');
        if (!modalEl || typeof bootstrap === 'undefined') return;
        var modal = bootstrap.Modal.getOrCreateInstance(modalEl);
        modal.show();
    }

    function findNewTaskLink() {
        var anchors = document.querySelectorAll('a[href]');
        for (var i = 0; i < anchors.length; i++) {
            var href = anchors[i].getAttribute('href') || '';
            if (/\/projects\/\d+\/tasks\/new$/.test(href)) {
                return anchors[i];
            }
        }
        return null;
    }

    document.addEventListener('keydown', function (e) {
        if (e.ctrlKey || e.metaKey || e.altKey) return;
        if (isTyping(e.target)) {

            if (e.key === 'Escape' && e.target && typeof e.target.blur === 'function') {
                e.target.blur();
            }
            return;
        }

        var key = e.key;

        if (key === '?') {
            e.preventDefault();
            openCheatsheet();
            return;
        }

        if (key === '/') {
            e.preventDefault();
            focusSearch();
            return;
        }

        if (key === 'n' && !pending) {
            var link = findNewTaskLink();
            if (link) {
                e.preventDefault();
                navigate(link.getAttribute('href'));
            }
            return;
        }

        if (key === 'Escape') {
            var dropdown = document.getElementById('searchResults');
            if (dropdown) dropdown.innerHTML = '';
            clearPending();
            return;
        }

        if (pending === 'g') {
            if (key === 'd') {
                e.preventDefault();
                clearPending();
                navigate('/dashboard');
                return;
            }
            if (key === 'p') {
                e.preventDefault();
                clearPending();
                navigate('/projects');
                return;
            }
            if (key === 's') {
                e.preventDefault();
                clearPending();
                navigate('/swagger-ui.html');
                return;
            }
            clearPending();
            return;
        }
        if (key === 'g') {
            pending = 'g';
            pendingTimer = setTimeout(clearPending, 900);
            return;
        }

        if (key === 't') {
            var btn = document.getElementById('themeToggle');
            if (btn) {
                e.preventDefault();
                btn.click();
            }
        }
    });
})();
