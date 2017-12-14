(function($, undefined) {

    $.fn.monthpicker = function(options) {

        var months = options.months || ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'],

            Monthpicker = function(el) {
                this._el = $(el);
                this._init();
                this._render();
                this._renderYears();
                this._renderMonths();
                this._bind();
            };

        Monthpicker.prototype = {
            destroy: function() {
                this._el.off('click');
                this._yearsSelect.off('click');
                this._container.off('click');
                $(document).off('click', $.proxy(this._hide, this));
                this._container.remove();
            },

            _init: function() {
                var current = '';
                if(this._el.is('input')) {
                    current = this._el.val();
                }
                else if(this._el.is('a')) {
                    current = this._el.html();
                }
                if(current == ''){
                    if(this._el.is('input')) {
                        this._el.val(options.years[0] + '-' + months[0]);
                    }
                    else if(this._el.is('a')) {
                        this._el.html(options.years[0] + '-' + months[0]);
                    }
                }
                this._el.data('monthpicker', this);
            },

            _bind: function() {
                this._el.on('click', $.proxy(this._show, this));
                $(document).on('click', $.proxy(this._hide, this));
                this._yearsSelect.on('click',
                    function(e) {
                        e.stopPropagation();
                    });
                this._container.on('click', 'button', $.proxy(this._selectMonth, this));
            },

            _show: function(e) {
                e.preventDefault();
                e.stopPropagation();
                var top = this._el.offset().top;
                var left = this._el.offset().left;
                this._container.css({'display': 'inline-block', 'top' : (top + this._el.height() + (options.topOffset || 0)) + 'px', 'left' : left + 'px'});
            },

            _hide: function() {
                this._container.css('display', 'none');
            },

            _selectMonth: function(e) {
                var monthIndex = $(e.target).data('value'),
                    month = months[monthIndex],
                    year = this._yearsSelect.val();
                if(this._el.is('input')) {
                    this._el.val(year + '-' + month);
                }
                else if(this._el.is('a')) {
                    this._el.html(year + '-' + month);
                }
                if (options.onMonthSelect) {
                    options.onMonthSelect(monthIndex, year);
                }
            },

            _render: function() {
                var top = this._el.offset().top;
                var left = this._el.offset().left;
                var cssOptions = {
                        display: 'none',
                        position: 'absolute',
                        top: top + this._el.height() + (options.topOffset || 0),
                        left: left
                    };
                this._id = (new Date).valueOf();
                this._container = $('<div class="monthpicker" id="monthpicker-' + this._id + '">').css(cssOptions).appendTo($('body'));
            },

            _renderYears: function() {
                var current = '';
                if(this._el.is('input')) {
                    current = this._el.val();
                }
                else if(this._el.is('a')) {
                    current = this._el.html();
                }
                var currentYear = '';
                if(current.length >0 ){
                    currentYear = current.substr(0,4);
                }

                var markup = $.map(options.years,
                    function(year) {
                        if(year == currentYear){
                            return '<option selected="selected">' + year + '</option>';
                        }
                        else{
                            return '<option>' + year + '</option>';
                        }

                    });
                var yearsWrap = $('<div class="years">').appendTo(this._container);
                this._yearsSelect = $('<select>').html(markup.join('')).appendTo(yearsWrap);
            },

            _renderMonths: function() {
                var markup = ['<table>', '<tr>'];
                $.each(months,
                    function(i, month) {
                        if (i > 0 && i % 4 === 0) {
                            markup.push('</tr>');
                            markup.push('<tr>');
                        }
                        markup.push('<td><button data-value="' + i + '">' + month + '</button></td>');
                    });
                markup.push('</tr>');
                markup.push('</table>');
                this._container.append(markup.join(''));
            }
        };

        var methods = {
            destroy: function() {
                var monthpicker = this.data('monthpicker');
                if (monthpicker) monthpicker.destroy();
                return this;
            }
        }

        if (methods[options]) {
            return methods[options].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof options === 'object' || !options) {
            return this.each(function() {
                return new Monthpicker(this);
            });
        } else {
            $.error('Method ' + options + ' does not exist on monthpicker');
        }

    };

} (jQuery));