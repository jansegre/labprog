BUILDDIR:=build

all: cmake
	@@make -C $(BUILDDIR)

cmake: $(BUILDDIR)
#	cd $(BUILDDIR) && cmake ..

$(BUILDDIR):
	mkdir $(BUILDDIR)

run: all
	@@$(BUILDDIR)/oscar

test: all
	@@$(BUILDDIR)/oscar_test
